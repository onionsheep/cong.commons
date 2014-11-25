/**
 * Created by cong on 2014/10/31.
 */
$(function () {
  $(".delete").on("click", function () {
    var del = confirm("确定删除？");
    if (!del) {
      return false;
    }
    $.post("./data", {
      t: "delete",
      m: "Admin",
      id: $(this).data("id")
    }, function (data) {
      data = $.parseJSON(data);
      if (data["result"] > 0) {
        alert("删除成功");
        location.reload(false);
      }
    });
  });
});