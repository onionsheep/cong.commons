$(function () {

  $("select[name=classID]").makeArtClassOptions();

  var classList = {};
  $("select[name=classID]").children("option").each(function () {
    classList[$(this).val()] = $(this).data("name");
  });
  $("table").find("td.classID").each(function () {
    var classID = $(this).data("classid");
    if (classList[classID]) {
      $(this).text(classList[classID]);
    } else {
      var oldText = $(this).text();
      $(this).text("栏目ID = " + oldText);
    }
  });

  $("select[name=classID]").on("change", function () {
    var v = $(this).val()
    location.href = "?path=article/list.jsp&classID=" + v;
  });

  $(".delete").on("click", function () {
    var del = confirm("确定删除？");
    if (!del) {
      return false;
    }
    $.post("./data", {
      t: "delete",
      m: "Article",
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