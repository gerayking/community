package life.gerayking.community.community.dto;

import life.gerayking.community.community.model.User;
import lombok.Data;

import java.net.UnknownServiceException;

@Data
public class NotificationDTO {
    private Long id;
    private Long receiver;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private Long outerId;
    private String typeName;
    private Integer type;
}

