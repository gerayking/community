//提交回复
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment-content").val();
    commentTarget(questionId, 1, content);
}

function commentTarget(targetId, type, content) {
    console.log(type);
    if (!content) {
        alert("无法回复空内容");
        return;
    }
    $.ajax({
        contentType: "application/json",
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "comment": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else if (response.code == 2003) {
                var isAccepted = confirm(response.message);
                if (isAccepted) {
                    window.open("https://github.com/login/oauth/authorize?client_id=7da624c9627d05f07e5b&redirect_uri=https://localhost:8887/callback&scope=user&state=1");
                    window.localStorage.setItem("closable", true);
                }
            } else {
                alert(response.message);
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var id = e.getAttribute("data-id");
    var content = $("#input-" + id).val();
    commentTarget(id, 2, content);
}

//展开二级评论,拼接
function collapseComments(e) {
    var id = e.getAttribute("data-id")
    var comment = $("#comment-" + id);

    var collapse = e.getAttribute("data-collapse");
    comment.toggleClass("in");
    if (comment.hasClass("in")) {
        $.getJSON("/comment/" + id, function (data) {
            debugger;
            var subCommentContainer = $("#comment-" + id);
            if(subCommentContainer.children().length == 2)//判断是否展开过了
            {
                $.each(data.data, function (index, comment) {
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body",
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html":comment.user.name
                    })).append($("<h5/>", {
                            "html":comment.comment
                    })).append($("<div/>", {
                            "class":"menu"
                    })).append($("<div/>", {
                        "class":"menu"
                    }).append($("<span/>", {
                            "class":"pull-right",
                            "html":moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                        })))
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded comment-head",
                        "src": comment.user.avatarUrl
                    }));
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
            }
        });
    }
}
function selectTag(e) {
    var tag = e.getAttribute("data");
    var previous = $("#tag").val();

    if(previous.indexOf(tag)==-1){
    if(previous){
        $("#tag").val(previous+','+tag);
    }
    else{
        $("#tag").val(tag);
    }
    }
}
function showSelectTag() {
    $("#select-tag").show();
}