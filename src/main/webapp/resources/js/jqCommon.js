var graphTemppateFilePath;
var indexProperty0;
var indexProperty1;
var indexProperty0Conn;
var indexProperty1Conn;

$(document).ready(function() {
    $.generateFormPatternSelection();
});

$.generateFormPatternSelection = function() {
    $("#formPatternSelection").removeAttr("hidden");
    // load json and add divs
    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                var selectId = "selectPtn" + patternName;
                var optionTags = "";
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
                '<option selected>==</option>' +
                '<option>&gt</option>' +
                '<option>&gt=</option>' +
                '<option>&lt</option>' +
                '<option>&lt=</option>' +
                '<option>!=</option>' +
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
        var afterScopeTag = '<div class="input-group">' +
            '<span class="input-group-addon">After: </span>' +
            '<input type="text" class="form-control" id="tfAfterScope" value="">' +
            '</div>';
        // var beforeScopeTag = '<div class="form-group" id="divBeforeScope">' +
        //     '<label for="beforeScope" class="control-label">Before: </label>' +
        //     '<div>' +
        //     '<input type="text" class="form-control" id="beforeScope">' +
        //     '</div></div>';
        var beforeScopeTag = '<div class="input-group">' +
            '<span class="input-group-addon">Before: </span>' +
            '<input type="text" class="form-control" id="tfBeforeScope" value="">' +
            '</div>';
        $(".templateDiv").prepend(afterScopeTag);
        $(".templateDiv").append(beforeScopeTag);
        $("#btnAddScope").parent().remove();
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
            property.op = $("#" + classNameOp).find("option:selected").text() || "";
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
    afters[0] = $("#tfAfterScope").val() || "";
    befores[0] = $("#tfBeforeScope").val() || "";
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
                '<option selected>かつ</option>' +
                '<option>または</option>' +
                '</select>' +
                '<input type="text" class="form-control col-md-5 col-sm-4" id="templateProperty0_' + indexProperty0 + '_op1">' +
                '<select class="form-control col-md-1 col-sm-1" id="templateProperty0_' + indexProperty0 + '_op">' +
                '<option selected>==</option>' +
                '<option>&gt</option>' +
                '<option>&gt=</option>' +
                '<option>&lt</option>' +
                '<option>&lt=</option>' +
                '<option>!=</option>' +
                '<input type="text" class="form-control" id="templateProperty0_' + indexProperty0 + '_op2">';
            '</div>';
            indexProperty0++;
            indexProperty0Conn++;
        } else if (btnIndex == 1) {
            divId = 'propertyGroup' + btnIndex + '_' + indexProperty1;
            var destTag = '<div class="input-group property-group-1" id="' + divId + '">' +
                '<select class="form-control col-md-1 col-sm-2" id="propertyConn' + btnIndex + '_' + indexProperty1Conn + '">' +
                '<option selected>かつ</option>' +
                '<option>または</option>' +
                '</select>' +
                '<input type="text" class="form-control col-md-5 col-sm-4" id="templateProperty1_' + indexProperty1 + '_op1">' +
                '<select class="form-control col-md-1 col-sm-1" id="templateProperty1_' + indexProperty1 + '_op">' +
                '<option selected>==</option>' +
                '<option>&gt</option>' +
                '<option>&gt=</option>' +
                '<option>&lt</option>' +
                '<option>&lt=</option>' +
                '<option>!=</option>' +
                '<input type="text" class="form-control" id="templateProperty1_' + indexProperty1 + '_op2">';
            '</div>';
            indexProperty1++;
            indexProperty1Conn++;
        }
        $(this).parent().after(destTag);
        $("#" + divId).append($(this));
    });
}

$.resetGlobalVars = function() {
    indexProperty0 = 0;
    indexProperty1 = 0;
    indexProperty0Conn = 0;
    indexProperty1Conn = 0;
}
