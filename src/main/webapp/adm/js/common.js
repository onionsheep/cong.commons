if (!String.prototype.trim) {
  (function () {
    // Make sure we trim BOM and NBSP
    var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
    String.prototype.trim = function () {
      return this.replace(rtrim, "");
    }
  })();
}


$.fn.serializeObject = function () {
  var o = {};
  var a = this.serializeArray();
  $.each(a, function () {
    if (o[this.name]) {
      if (!o[this.name].push) {
        o[this.name] = [o[this.name]];
      }
      o[this.name].push(this.value || '');
    } else {
      o[this.name] = this.value || '';
    }
  });
  return o;
};

$.fn.makeArtClassOptions = function () {
  var select = $(this);
  var options = select.children("option");
  options.each(function () {
    var e = $(this);
    var pid = e.data("pid");
    if(e.attr("value") - pid != 0){//防止出现自己作为自己的父级栏目，程序BUG掉的情况
      while (pid - 0 > 0) {
        var pe = options.filter("[value=" + pid + "]");
        var pname = pe.text().trim();
        var oldName = e.text().trim();
        e.text(pname + " -- " + oldName);
        pid = pe.data("pid");
      }
    }
  });
}

