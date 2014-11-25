$(function () {

  $("#classFilter").on("change", function () {
    var v = $(this).val()
    location.href = v;
  });

  var okbtn = $("#modal-ok-btn");
  var messageModal = $("#messageModal");
  var messageForm = $("#messageForm");
  okbtn.on("click", function () {
    var formObject = messageForm.serializeObject();
    $.post("./data", {
      t: "update",
      m: "Message",
      ignoreNull : true,
      Message: JSON.stringify(formObject)
    }, function (data) {
      data = $.parseJSON(data);
      if (data["result"] > 0) {
        alert("回复成功");
        location.reload(false);
      }
    });
    return false;
  });

  $("a.reply").on("click", function () {

    var msgid = $(this).data("id");
    var form = messageForm;
    $.get("./data", {
      t: "get",
      m: "Message",
      "id": msgid
    }, function (data) {
      data = $.parseJSON(data);
      var m = data["Message"];
      for(var name in m){
        var value = m[name];
        $("#modal-"+ name).val(value);
      }
    });
    messageModal.modal();
    return false;
  });

  $("a.delete").on("click", function () {
    var del = confirm("确定删除？");
    if (!del) {
      return false;
    }

    $.post("./data", {
      t: "delete",
      m: "Message",
      id: $(this).data("id")
    }, function (data) {
      data = $.parseJSON(data);
      if (data["result"] > 0) {
        alert("删除成功");
        location.reload(false);
      }
    });
    return false;
  });

});