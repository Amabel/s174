var graphTemppateFilePath;
var indexProperty0;
var indexProperty1;
var indexProperty0Conn;
var indexProperty1Conn;
var indexAfter;
var indexBefore;
var indexAfterConn;
var indexBeforeConn;

$(document).ready(function() {
    $.addBtnHeaderListener();
    $.generateFormPatternSelection();
});

$.generateFormPatternSelection = function() {
    $("#formPatternSelection").removeAttr("hidden");
    // load json and add divs
    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                var selectId = "selectPtn" + patternName;
                var optionTags = '<option value="none" hidden></option>\n';
                for (var i = 0; i < pattern.templates.length; i++) {
                    optionTags += '<option value="' + i + '">' + pattern.templates[i] + '</option>\n';
                }
                $("#patternSelection").append(
                    '<div class="form-group">' +
                    '<label for="' + selectId + '">' + pattern.name + '： </label><br>' +
                    '<label>' + pattern.intent + '</label>' +
                    '<select class="form-control" name="' + selectId + '" id="' + selectId + '">' +
                    optionTags +
                    '</select>' +
                    '</div>'
                );
                $("#" + selectId).val([]);
                $.addSelectListener(selectId, pattern.templates);
            });

        })
    });
}

$.addSelectListener = function(selectId, templates) {
    // reset params
    $.resetGlobalVars();

    $("#" + selectId).change(function() {
        var val = $(this).val();
        $.removeSelects();
        $(this).val(val);
        $(".templateDiv").remove();
        $("#btnSubmitTemplate").remove();
        $("#btnDownloadTemplate").remove();
        $("#retDiv").remove();
        var optValue = $(this).children('option:selected').val();
        $.showTemplate(selectId, templates[optValue]);
    });
}

$.showTemplate = function(selectId, template) {
    var reg = new RegExp("〇〇", "g");
    var replacedTemplate = template.replace(reg, ",property,");
    var templateArray = replacedTemplate.split(",");

    var tags = '<div class="templateDiv" id="' + selectId + 'Template">' +
        '<div class="propertyDiv">';
    // var tags = '<div class="templateDiv input-group" id="templateDiv">';
    var propertyIndex = 0;
    var flagBr = false;
    var tag = "";
    for (var i = 0, j = 0; i < templateArray.length; i++) {
        if (templateArray[i] == "property") {
            tag += '<input type="text" class="form-control" id="templateProperty' + propertyIndex + '_0_op1">' +
                '<select class="form-control col-md-1 col-sm-1" id="templateProperty' + propertyIndex + '_0_op">' +
                '<option selected value=""></option>' +
                '<option value="==">=</option>' +
                '<option value="&gt">&gt</option>' +
                '<option value="&gt=">≧</option>' +
                '<option value="&lt">&lt</option>' +
                '<option value="&lt=">≦</option>' +
                '<option value="!=">≠</option>' +
                '<input type="text" class="form-control" id="templateProperty' + propertyIndex + '_0_op2">';
            propertyIndex++;
        } else if (i > 0 && templateArray[i] != "") {
            tag += '<span class="input-group-addon addPropertyButton" id="btnAddProperty' + j + '" >' + templateArray[i] + '</span>';
            j++;
            flagBr = true;
        } else if (templateArray[i] != "") {
            tag += '<span class="input-group-addon">' + templateArray[i] + '</span>';
        }
        if (flagBr) {
            if (j == 1) {
                tag = '<div class="input-group property-group-0" id="propertyGroup0_' + indexProperty0 + '">' + tag + '</div>';
                indexProperty0++;
            } else if (j == 2) {
                tag = '<div class="input-group property-group-1" id="propertyGroup1_' + indexProperty1 + '">' + tag + '</div>';
                indexProperty1++;
            }
            flagBr = false;
            tags += tag;
            tag = "";
        }
    }
    // for (var i = 0, j = 0; i < templateArray.length; i++) {
    //     tags += '<div class="input-group" id="propertyGroup' + i + '">';
    //     if (templateArray[i] == "property") {
    //         tags += '<input type="text" class="form-control" id="templateProperty' + propertyIndex + '">';
    //         propertyIndex++;
    //     } else if (i > 0 && templateArray[i] != "") {
    //         tags += '<span class="input-group-addon addPropertyButton" id="btnAddProperty' + j + '" >' + templateArray[i] + '</span>';
    //         j++;
    //     } else if (templateArray[i] != "") {
    //         tags += '<span class="input-group-addon">' + templateArray[i] + '</span>';
    //     }
    //     tags += '</div>';
    // }
    tags += '</div></div>';
    $("#" + selectId).after(
        tags
    );

    $(".templateDiv ").after(
        // add btn
        '<div class="text-right">' +
        '<button type="button" class="btn btn-success btnSubmit" id="btnSubmitTemplate">Submit</button>' +
        '</div>'
    );
    $(".templateDiv ").hide()
    $("#btnSubmitTemplate").hide();
    $(".templateDiv").fadeIn();
    $("#btnSubmitTemplate").fadeIn();
    $("#btnSubmitTemplate").click(function() {
        var patternName = selectId.substr(9);
        $.submitTemplate(patternName, propertyIndex);
    });
    var addScopeTag = '<div>' +
        '<button type="button" class="btn btn-primary btnAddScope" id="btnAddScope">Add scope</button>' +
        '</div>';
    $(".templateDiv").prepend(addScopeTag);
    $.updateTags();
    $.addPropertyButtonListener();
    $.addAddScopeListener();
}

$.addAddScopeListener = function() {
    $("#btnAddScope").click(function() {
        // var afterScopeTag = '<div>' +
        //     '<div class="form-group" id="divAfterScpoe">' +
        //     '<label class="scopeLabel control-label" for="afterScope">After: </label>' +
        //     '<div>' +
        //     '<input type="text" class="form-control" id="afterScope">' +
        //     '</div></div></div>';
        var afterScopeTag = '<div class="scopeAfter">' +
            '<div class="input-group scope-after" id="scpoeAfter' + indexAfter + '">' +
            '<span class="input-group-addon" id="btnAddAfter">After: </span>' +
            '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op1" value="">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
            '<option selected value=""></option>' +
            '<option value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op2" value="">' +
            '</div>' +
            '</div>';
        indexAfter++;
        // var beforeScopeTag = '<div class="form-group" id="divBeforeScope">' +
        //     '<label for="beforeScope" class="control-label">Before: </label>' +
        //     '<div>' +
        //     '<input type="text" class="form-control" id="beforeScope">' +
        //     '</div></div>';
        var beforeScopeTag = '<div class="scopeBefore">' +
            '<div class="input-group scope-before" id="scopeBefore' + indexBefore + '">' +
            '<span class="input-group-addon" id="btnAddBefore">Before: </span>' +
            '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op1" value="">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfBeforeScope' + indexBefore + '_op">' +
            '<option selected value=""></option>' +
            '<option value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op2" value="">' +
            '</div>' +
            '</div>';
        indexBefore++;
        $(".templateDiv").prepend(afterScopeTag);
        $(".templateDiv").append(beforeScopeTag);
        $("#btnAddScope").parent().remove();
        $.addScopeButtonListener();
    });
}

$.addScopeButtonListener = function() {
    $("#btnAddAfter").click(function() {
        divId = 'scopeAfter' + indexAfter;
        var destTag = '<div class="input-group scope-after" id="' + divId + '">' +
            '<select class="form-control col-md-1 col-sm-2 " id="scopeAfterConn' + indexAfterConn + '">' +
            '<option value="かつ" selected>かつ</option>' +
            '<option value="または">または</option>' +
            '</select>' +
            '<input type="text" class="form-control col-md-5 col-sm-4" id="tfAfterScope' + indexAfter + '_op1">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
            '<option selected value=""></option>' +
            '<option value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op2">';
        '</div>';
        indexAfter++;
        indexAfterConn++;
        $("div.scopeAfter").append(destTag);
    });

    $("#btnAddBefore").click(function() {
        divId = 'scopeAfter' + indexBefore;
        var destTag = '<div class="input-group scope-before" id="' + divId + '">' +
            '<select class="form-control col-md-1 col-sm-2 " id="scopeBeforeConn' + indexBeforeConn + '">' +
            '<option value="かつ" selected>かつ</option>' +
            '<option value="または">または</option>' +
            '</select>' +
            '<input type="text" class="form-control col-md-5 col-sm-4" id="tfBeforeScope' + indexBefore + '_op1">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfBeforeScope' + indexBefore + '_op">' +
            '<option selected value=""></option>' +
            '<option value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op2">';
        '</div>';
        indexBefore++;
        indexBeforeConn++;
        $("div.scopeBefore").append(destTag);
    });
}

$.submitTemplate = function(patternName, numProperty) {
    // console.log("pattern name: " + patternName + ", number: " + numProperty);
    // find properties
    // var params = new Array();
    // for (var i = 0; i < numProperty; i++) {
    //     console.log("param " + i + ": " + $("#templateProperty" + i).val());
    //     params[i] = $("#templateProperty" + i).val();
    // }
    // find params
    var params = new Array();
    for (var i = 0; i < numProperty; i++) {
        var properties = new Array();
        var numSubProperty = $(".property-group-" + i).length;
        console.log("num property-group-" + "i: " + numSubProperty);
        for (var j = 0; j < numSubProperty; j++) {
            var property = new Object();
            var classNameOp1 = "templateProperty" + i + "_" + j + "_op1";
            var classNameOp = "templateProperty" + i + "_" + j + "_op";
            var classNameOp2 = "templateProperty" + i + "_" + j + "_op2";
            property.op1 = $("#" + classNameOp1).val() || "";
            property.op2 = $("#" + classNameOp2).val() || "";
            property.op = $("#" + classNameOp).val() || "";
            if (j > 0) {
                var k = j - 1;
                var id = "propertyConn" + i + "_" + k;
                var conn = $("#" + id).find("option:selected").text();
                if (conn == "かつ") {
                    property.connector = "&&";
                } else if (conn == "または") {
                    property.connector = "||";
                }
            } else {
                property.connector = "";
            }
            // console.log("j = " + j + ", pp = " + property.op1 + " " + property.op + " " + property.op2);
            properties.push(property);
        }
        params.push(properties);
    }


    // find scope
    var afters = new Array();
    var befores = new Array();
    // afters[0] = $("#tfAfterScope").val() || "";
    // befores[0] = $("#tfBeforeScope").val() || "";

    // find after
    var numScopeAfter = $("div.scope-after").length;
    for (var i = 0; i < numScopeAfter; i++) {
        var after = new Object();
        var idOp1 = "tfAfterScope" + i + "_op1";
        var idOp = "tfAfterScope" + i + "_op";
        var idOp2 = "tfAfterScope" + i + "_op2";
        after.op1 = $("#" + idOp1).val() || "";
        after.op2 = $("#" + idOp2).val() || "";
        after.op = $("#" + idOp).val() || "";
        if (i > 0) {
            var j = i - 1;
            var id = "scopeAfterConn" + j;
            var conn = $("#" + id).find("option:selected").text();
            if (conn == "かつ") {
                after.connector = "&&";
            } else if (conn == "または") {
                after.connector = "||";
            }
        } else {
            after.connector = "";
        }
        afters.push(after);
    }

    //fing before
    var numScopeBefore = $("div.scope-before").length;
    for (var i = 0; i < numScopeBefore; i++) {
        var before = new Object();
        var idOp1 = "tfBeforeScope" + i + "_op1";
        var idOp = "tfBeforeScope" + i + "_op";
        var idOp2 = "tfBeforeScope" + i + "_op2";
        before.op1 = $("#" + idOp1).val() || "";
        before.op2 = $("#" + idOp2).val() || "";
        before.op = $("#" + idOp).val() || "";
        if (i > 0) {
            var j = i - 1;
            var id = "scopeBeforeConn" + j;
            var conn = $("#" + id).find("option:selected").text();
            if (conn == "かつ") {
                before.connector = "&&";
            } else if (conn == "または") {
                before.connector = "||";
            }
        } else {
            before.connector = "";
        }
        befores.push(before);
    }

    // new json
    var templateJson = new Object();
    templateJson.pattern = patternName;
    templateJson.params = params;
    templateJson.afters = afters;
    templateJson.befores = befores;

    console.log(templateJson);


    // send json
    $.ajax({
        type: 'post',
        url: 'template',
        dataType: 'JSON',
        data: {
            templateJson: JSON.stringify(templateJson)
        },
        success: function(data) {
            // alert("success");
            retJson = JSON.parse(data);
            console.log(retJson);
            graphTemppateFilePath = retJson.graphTemplateFileName;
            $.showDownloadButton();
            $.showLTLFormula(retJson.ltl);
        },
        error: function(data) {
            alert('failed');
        }
    });

}

$.showLTLFormula = function(ltl) {
    $("#LTLFormula").remove();
    var ltlFormulaTag = '<span id="LTLFormula">' +
        "LTLFormula: <br>" +
        "pattern: " + ltl.pattern + "<br>" +
        "scope: " + ltl.scope + "<br>" +
        "ltl: " + ltl.formula + "<br>" +
        "</span>";
    $("#btnDownloadTemplate").before(ltlFormulaTag);
}

$.showDownloadButton = function() {
    // remove previous download mark
    $("#retDiv").remove();
    $("#btnDownloadTemplate").remove();
    var buttonTag = '<div id="retDiv">' +
        // '<img title="Download XML file" src="resources/images/download-button.png" class = "btn form-control" id="btnDownloadTemplate" alt="Download XML file" />' +
        '<span id="btnDownloadTemplate">download XML file</span>' +
        '</div>';

    // append to submit button
    $("#btnSubmitTemplate").parent().after(buttonTag);
    $.addDownloadTemplateButtonListener();
}

$.addDownloadTemplateButtonListener = function() {
    $("#btnDownloadTemplate").click(function() {


        // $.fileDownload(graphTemppateFilePath);
        $.ajax({
            type: 'post',
            url: 'download/graph-template',
            dataType: 'text',
            data: {
                // filename: JSON.stringify(graphTemppateFilePath)
                filename: graphTemppateFilePath
            },
            success: function(data) {
                // alert("success");
                // console.log(data);
                // var url = this.url + "s/" + graphTemppateFilePath;
                var form = $('<form></form>').attr('action', this.url).attr('method', 'post');
                // Add the one key/value
                form.append($("<input></input>").attr('type', 'hidden').attr('name', "filename").attr('value', graphTemppateFilePath));
                //send request
                form.appendTo('body').submit().remove();

                console.log('file downloaded');
            },
            error: function(data) {
                alert('fail');
            }
        });
    });
}

$.addPropertyButtonListener = function() {
    $(".addPropertyButton").click(function(event) {
        // console.log($(this).attr("id"));
        var btnIndex = $(this).attr("id").substr(-1);
        var divId = "";
        if (btnIndex == 0) {
            divId = 'propertyGroup' + btnIndex + '_' + indexProperty0;
            var destTag = '<div class="input-group property-group-0" id="' + divId + '">' +
                '<select class="form-control col-md-1 col-sm-2 " id="propertyConn' + btnIndex + '_' + indexProperty0Conn + '">' +
                '<option value="かつ" selected>かつ</option>' +
                '<option value="または">または</option>' +
                '</select>' +
                '<input type="text" class="form-control col-md-5 col-sm-4" id="templateProperty0_' + indexProperty0 + '_op1">' +
                '<select class="form-control col-md-1 col-sm-1" id="templateProperty0_' + indexProperty0 + '_op">' +
                '<option selected value=""></option>' +
                '<option value="==">=</option>' +
                '<option value="&gt">&gt</option>' +
                '<option value="&gt=">≧</option>' +
                '<option value="&lt">&lt</option>' +
                '<option value="&lt=">≦</option>' +
                '<option value="!=">≠</option>' +
                '<input type="text" class="form-control" id="templateProperty0_' + indexProperty0 + '_op2">';
            '</div>';
            indexProperty0++;
            indexProperty0Conn++;
        } else if (btnIndex == 1) {
            divId = 'propertyGroup' + btnIndex + '_' + indexProperty1;
            var destTag = '<div class="input-group property-group-1" id="' + divId + '">' +
                '<select class="form-control col-md-1 col-sm-2" id="propertyConn' + btnIndex + '_' + indexProperty1Conn + '">' +
                '<option value="かつ" selected>かつ</option>' +
                '<option value="または">または</option>' +
                '</select>' +
                '<input type="text" class="form-control col-md-5 col-sm-4" id="templateProperty1_' + indexProperty1 + '_op1">' +
                '<select class="form-control col-md-1 col-sm-1" id="templateProperty1_' + indexProperty1 + '_op">' +
                '<option selected value=""></option>' +
                '<option value="==">=</option>' +
                '<option value="&gt">&gt</option>' +
                '<option value="&gt=">≧</option>' +
                '<option value="&lt">&lt</option>' +
                '<option value="&lt=">≦</option>' +
                '<option value="!=">≠</option>' +
                '<input type="text" class="form-control" id="templateProperty1_' + indexProperty1 + '_op2">';
            '</div>';
            indexProperty1++;
            indexProperty1Conn++;
        }
        $(this).parent().after(destTag);
        $("#" + divId).append($(this));
        $.updateTags();
    });
}

$.resetGlobalVars = function() {
    indexProperty0 = 0;
    indexProperty1 = 0;
    indexProperty0Conn = 0;
    indexProperty1Conn = 0;
    indexAfter = 0;
    indexBefore = 0;
    indexAfterConn = 0;
    indexBeforeConn = 0;
}

$.updateTags = function() {
    $(".property-group-0").each(function() {
        $(this).removeClass("last");
    });
    $(".property-group-0:last").addClass("last");

    $(".property-group-1").each(function() {
        $(this).removeClass("last");
    });
    $(".property-group-1:last").addClass("last");

    $(".property-group-2").each(function() {
        $(this).removeClass("last");
    });
    $(".property-group-2:last").addClass("last");
}

$.removeSelects = function() {
    $("select").val("none");
}

$.addBtnHeaderListener = function() {
    $("#btnHeader").click(function() {
        $.resetGlobalVars();
        $.removeSelects();
        $(".templateDiv").remove();
        $("#btnSubmitTemplate").remove();
        $("#btnDownloadTemplate").remove();
        $("#retDiv").remove();
    });
}
