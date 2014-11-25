/**
 * Created by cong on 2014/10/30.
 */

$(function () {
  $("button[type=submit]").click(function () {
    var formObj = $("form").serializeObject();
    $.post("./data", {
      t: "update",
      m: "Site",
      "Site": JSON.stringify(formObj)
    }, function (data) {
      data = $.parseJSON(data);
      if (data["result"] > 0) {
        alert("修改成功");
        location.reload(false);
      }
    })
    return false;
  });
});