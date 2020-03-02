package life.gerayking.community.community.controller;

import life.gerayking.community.community.dto.FileDTO;
import life.gerayking.community.community.provider.AliyunOssProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private AliyunOssProvider aliyunOssProvider;
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(@RequestParam("editormd-image-file")MultipartFile file, Model model){
      FileDTO fileDTO = new FileDTO();
      String filename = file.getOriginalFilename();
      try{
          if(file!=null){
              if(!"".equals(filename.trim())){
                  File newFile = new File(filename);
                  FileOutputStream outputStream = new FileOutputStream(newFile);
                  outputStream.write(file.getBytes());
                  outputStream.close();
                  file.transferTo(newFile);
                  String url = aliyunOssProvider.upload(newFile);
                  fileDTO.setSuccess(1);
                  fileDTO.setUrl(url);
                  fileDTO.setMessage("上传成功");
              }
          }
      } catch (FileNotFoundException e) {

          e.printStackTrace();
      } catch (IOException e) {
          fileDTO.setSuccess(0);
          fileDTO.setMessage("上传失败");
          e.printStackTrace();
      }
      return fileDTO;
    }
}
