$(function () {
  $("#addArtClass").click(function () {
    $("#addArtClassOK").data("type", "add");
    $("#addArtClassModal .modal-title").text("添加栏目");
    $("#addArtClassModal").modal();
    return false;
  });

  $("select[name=parentID]").makeArtClassOptions();

  $("#addArtClassOK").on("click", function () {
    var formObject = $("#addArtClassForm").serializeObject();
    var optype = $("#addArtClassOK").data("type");
    $.post("./data", {
      t: optype,
      m: "ArtClass",
      "ArtClass": JSON.stringify(formObject)
    }, function (data) {
      data = $.parseJSON(data);
      if (data["result"] > 0) {
        if (optype == "add") {
          alert("添加成功");
        } else if (optype == "update") {
          alert("修改成功");
        }
        location.reload(false);
      }
    });

    return false;
  });

  $("a.edit").on("click", function () {
    $("#addArtClassOK").data("type", "update");
    $("#addArtClassModal .modal-title").text("修改栏目");
    var tr = $(this).parent().parent();
    var form = $("#addArtClassForm");
    $.get("./data", {
      t: "get",
      m: "ArtClass",
      "id": $(tr).find("td[data-id=1]").text()
    }, function (data) {
      data = $.parseJSON(data);
      var ac = data["ArtClass"];
      for (var name in ac) {
        var input = form.find("input[name='" + name + "']");
        if (input.attr("type") == "text") {
          input.val(ac[name]);
        } else if (input.attr("type") == "radio") {
          input.removeAttr("checked");
          input.filter("input[value=" + ac[name] + "]")[0].checked = true;
        }

        var select = form.find("select[name='" + name + "']");
        if (select.length > 0) {
          var selectedOption = select.children("option[value='" + ac[name] + "']");
          selectedOption.attr("selected", "selected")
        }


      }
    });
    $("#addArtClassModal").modal();
    return false;
  });


  $("a.delete").on("click", function () {
    var del = confirm("确定删除？");
    if (!del) {
      return false;
    }
    //var tr = $(this).parent().parent();
    //var id = $(tr).find("td[data-id=1]").text();
    $.post("./data", {
      t: "delete",
      m: "ArtClass",
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