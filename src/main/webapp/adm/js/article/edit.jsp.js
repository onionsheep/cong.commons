$(function () {
  $("select[name=classID]").makeArtClassOptions();
  //window.UEDITOR_HOME_URL = "/adm/ueditor/"
  var ue = UE.getEditor('editor', {});
  $("#editor").removeClass("form-control");

  $("input[type=submit]").on("click", function () {
    var form = $("#editform").serializeObject();
    var autoID = form["autoID"];
    var op;
    var info;
    if (autoID) {
      op = "update";
      info = "修改";
    } else {
      op = "add";
      info = "添加";
    }

    $.post("./data", {
      t: op,
      m: "Article",
      "Article": JSON.stringify(form)
    }, function (data) {
      var msg = $.parseJSON(data);
      if (msg["result"] > 0) {
        alert(info + "成功");
        location.replace(document.referrer);
      }
    });
    return false;
  });


});