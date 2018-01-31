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

    // $("#fileInput").fileinput({
    //     showCaption: false,
    //     uploadUrl: 'upload', // you must set a valid URL here else you will get an error
    //     allowedFileExtensions: ['z151'],
    //});


    $("#fileInput").fileinput({
        showPreview: false,
        showUpload: false,
        maxFileSize: 300,
        elErrorContainer: '#kartik-file-errors',
        allowedFileExtensions: ["z151", "png"],
        uploadUrl: 'upload'
    });

    $('#fileInput').on('fileuploaded', function(event, data, previewId, index) {
        // console.log(data);
        var ltlFormulaTag = '<span id="LTLFormula">' +
            "LTLFormula: <br>" +
            "pattern: " + data.response.ltl.pattern + "<br>" +
            "scope: " + data.response.ltl.scope + "<br>" +
            "ltl: " + data.response.ltl.formula + "<br>" +
            "</span>";
        $("#ltlResults").append(ltlFormulaTag);
    });

    $("#closeModal").click(function() {
        $("#ltlResults").empty();
        $("#fileInput").fileinput('clear');
    });

    $("#fileInput").on("fileuploaderror", function(event, data, previewId, index) {
        console.log("error");
    });

    $("#uploadXML").click(function() {
        $("#ltlResults").empty();
        $("#fileInput").fileinput('upload');
    })

    $.addBtnHeaderListener();
    $.addBtnResetListener();
    $.generateFormPatternSelection();
});

$.showScopeSelection = function() {
    var scopeSelectionTag = '<label>スコープのキーワード選択</label>' +
        '<select class="form-control" id="scopeSelect">' +
        '<option value="global" selected>どんなときでも、</option>' +
        '<option value="after">〇〇の後で、</option>' +
        '<option value="before">〇〇の前に</option>' +
        '<option value="between">〇〇から〇〇の間で、</option>' +
        '</select>';
    $("#scopeSelection").append(scopeSelectionTag);
}

$.generateFormPatternSelection = function() {
    var uniqueMode = true;
    if (uniqueMode == true) {
        $.showScopeSelection();
        $.generateUniquePatternSelection();
    } else {
        $.generateSeparatePatternSelection();
    }
}

$.generateUniquePatternSelection = function() {
    // $("#formPatternSelection").removeAttr("hidden");

    // var propertyPatternJson = $.construcuJsonObject();

    // $.showPatternSelection(propertyPatternJson);

    $.loadJsonAndAddDivs();
}

$.showPatternSelection = function(propertyPatternJson) {
    var optionTags = '<option value="none" selected hidden></option>\n';

    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                for (var i = 0; i < pattern.templates.length; i++) {
                    optionTags += '<option>' + pattern.templates[i] + '</option>\n';
                }
            });

        });
    });

    var selectTag = '<div class="form-group">' +
        '<label for="patternSelect">パターンのキーワード選択</label><br>' +
        '<select class="form-control" name="patternSelect" id="patternSelect">' +
        optionTags +
        '</select>' +
        '</div>';

    $("#patternSelection").append(selectTag);
}

$.construcuJsonObject = function() {
    var propertyPatternJson = new Array();

    // construct json object
    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                var propertyPattern = new Object();
                propertyPattern.patternName = patternName;
                propertyPattern.name = pattern.name;
                propertyPattern.intent = pattern.intent;
                propertyPattern.templates = pattern.templates;
                // propertyPatternJson.push(propertyPattern);
                propertyPatternJson[key] = propertyPattern;
            });
        });
    });
    console.log(propertyPatternJson);
    return propertyPatternJson;
}

$.loadJsonAndAddDivs = function() {
    // load json and add divs
    var optionTags = '<option selected value="none" hidden></option>';
    var templates = new Array();
    $.getJSON("resources/common/patterns.json", function(data) {
        var j = 0;
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                // var selectId = "selectPtn" + patternName;
                for (var i = 0; i < pattern.templates.length; i++) {
                    if (j == 0) {
                        optionTags += '<option disabled>--- 応答パターン ---</option>\n';
                    } else if (j == 1) {
                        optionTags += '<option disabled>--- 普遍パターン ---</option>\n';
                    } else if (j == 4) {
                        optionTags += '<option disabled>--- 不在パターン ---</option>\n';
                    } else if (j == 6) {
                        optionTags += '<option disabled>--- 先行パターン ---</option>\n';
                    } else if (j == 8) {
                        optionTags += '<option disabled>--- 存在パターン ---</option>\n';
                    } else if (j == 11) {
                        optionTags += '<option disabled>--- 存在パターン（2回） ---</option>\n';
                    }
                    optionTags += '<option value="' + j + '">' + pattern.templates[i] + '</option>\n';
                    j++;
                    templates.push(pattern.templates[i]);
                }
            });
        });
        $("#patternSelection").append(
            '<div class="form-group">' +
            '<label for="patternSelect">パターンのキーワード選択</label><br>' +
            '<select class = "form-control" name="pattern-select" id="patternSelect">' +
            optionTags +
            '</select>' +
            '</div>'
        );
        var selectId = "patternSelect";
        $("#" + selectId).val([]);
        $.addUniqueSelectListener(selectId, templates);
    });
}

$.addUniqueSelectListener = function(selectId, templates) {
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
        var optText = $(this).children('option:selected').text();
        console.log(optText);
        $.showTemplate(selectId, optText);
    });
}

$.generateSeparatePatternSelection = function() {
    $("#formPatternSelection").removeAttr("hidden");
    // load json and add divs
    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            $.each(patterns, function(patternName, pattern) {
                var selectId = "selectPtn" + patternName;
                var optionTags = '<option value="none" hidden></option>\n';
                for (var i = 0; i < pattern.templates.length; i++) {
                    if (i == 0) {
                        optionTags += '<option>000</option>\n';
                    }
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
        });
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
                '<option value=""></option>' +
                '<option selected value="==">=</option>' +
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
        // var patternName = selectId.substr(9);
        $.getJSON("resources/common/patterns.json").done(function(data) {
            var proppattern = "";
            var selectedTemplate = $("#patternSelect").find("option:selected").text();
            $.each(data.property_patterns, function(key, patterns) {
                $.each(patterns, function(patternName, pattern) {
                    for (var i = 0; i < pattern.templates.length; i++) {
                        if (selectedTemplate == pattern.templates[i]) {
                            proppattern = patternName;
                            break;
                        }
                    }
                });
            });
            $.submitTemplate(proppattern, propertyIndex);
        });

    });
    // var addScopeTag = '<div>' +
    //     '<button type="button" class="btn btn-primary btnAddScope" id="btnAddScope">Add scope</button>' +
    //     '</div>';
    // $(".templateDiv").prepend(addScopeTag);
    $.updateTags();
    $.addPropertyButtonListener();
    // $.addAddScopeListener();
    // $.addScopeTemplate();
    $.addUniqueScopeTemplate();

}

$.addUniqueScopeTemplate = function() {
    var scope = $("#scopeSelect").val();
    console.log(scope);

    switch (scope) {
        case "between":
            $.generateBeforeTag();
            $.generateAfterTag();
            break;
        case "after":
            $.generateAfterTag();
            break;
        case "before":
            $.generateBeforeTag();
            break;
        case "global":
            break;
    }
    $.addScopeButtonListener();
}

$.generateAfterTag = function() {
    var afterScopeTag = '<div class="scopeAfter">' +
        '<div class="input-group scope-after" id="scpoeAfter' + indexAfter + '">' +
        '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op1" value="">' +
        '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
        '<option value=""></option>' +
        '<option selected value="==">=</option>' +
        '<option value="&gt">&gt</option>' +
        '<option value="&gt=">≧</option>' +
        '<option value="&lt">&lt</option>' +
        '<option value="&lt=">≦</option>' +
        '<option value="!=">≠</option>' +
        '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op2" value="">' +
        '<span class="input-group-addon" id="btnAddAfter">の後で、</span>' +
        '</div>' +
        '</div>';
    indexAfter++;
    $(".templateDiv").prepend(afterScopeTag);
}

$.generateBeforeTag = function() {
    var beforeScopeTag = '<div class="scopeBefore">' +
        '<div class="input-group scope-before" id="scopeBefore' + indexBefore + '">' +
        '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op1" value="">' +
        '<select class="form-control col-md-1 col-sm-1" id="tfBeforeScope' + indexBefore + '_op">' +
        '<option value=""></option>' +
        '<option selected value="==">=</option>' +
        '<option value="&gt">&gt</option>' +
        '<option value="&gt=">≧</option>' +
        '<option value="&lt">&lt</option>' +
        '<option value="&lt=">≦</option>' +
        '<option value="!=">≠</option>' +
        '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op2" value="">' +
        '<span class="input-group-addon" id="btnAddBefore">の前に</span>' +
        '</div>' +
        '</div>';
    indexBefore++;
    $(".templateDiv").prepend(beforeScopeTag);
}

$.addScopeTemplate = function() {
    var afterScopeTag = '<div class="scopeAfter">' +
        '<div class="input-group scope-after" id="scpoeAfter' + indexAfter + '">' +
        '<span class="input-group-addon" id="btnAddAfter">After: </span>' +
        '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op1" value="">' +
        '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
        '<option value=""></option>' +
        '<option selected value="==">=</option>' +
        '<option value="&gt">&gt</option>' +
        '<option value="&gt=">≧</option>' +
        '<option value="&lt">&lt</option>' +
        '<option value="&lt=">≦</option>' +
        '<option value="!=">≠</option>' +
        '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op2" value="">' +
        '</div>' +
        '</div>';
    indexAfter++;
    var beforeScopeTag = '<div class="scopeBefore">' +
        '<div class="input-group scope-before" id="scopeBefore' + indexBefore + '">' +
        '<span class="input-group-addon" id="btnAddBefore">Before: </span>' +
        '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op1" value="">' +
        '<select class="form-control col-md-1 col-sm-1" id="tfBeforeScope' + indexBefore + '_op">' +
        '<option value=""></option>' +
        '<option selected value="==">=</option>' +
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
}

$.addAddScopeListener = function() {
    $("#btnAddScope").click(function() {
        var afterScopeTag = '<div class="scopeAfter">' +
            '<div class="input-group scope-after" id="scpoeAfter' + indexAfter + '">' +
            '<span class="input-group-addon" id="btnAddAfter">After: </span>' +
            '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op1" value="">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
            '<option value=""></option>' +
            '<option selected value="==">=</option>' +
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
            '<option value=""></option>' +
            '<option selected value="==">=</option>' +
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
    var divId = "";
    $("#btnAddAfter").click(function() {
        divId = 'scopeAfter' + indexAfter;
        var destTag = '<div class="input-group scope-after" id="' + divId + '">' +
            '<select class="form-control col-md-1 col-sm-2 " id="scopeAfterConn' + indexAfterConn + '">' +
            '<option value="かつ" selected>かつ</option>' +
            '<option value="または">または</option>' +
            '</select>' +
            '<input type="text" class="form-control col-md-5 col-sm-4" id="tfAfterScope' + indexAfter + '_op1">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfAfterScope' + indexAfter + '_op">' +
            '<option value=""></option>' +
            '<option selected value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfAfterScope' + indexAfter + '_op2">';
        '</div>';
        indexAfter++;
        indexAfterConn++;
        // $("div.scopeAfter").append(destTag);
        $(this).parent().after(destTag);
        $("#" + divId).append($(this));
    });

    $("#btnAddBefore").click(function() {
        divId = 'scopeBefore' + indexBefore;
        var destTag = '<div class="input-group scope-before" id="' + divId + '">' +
            '<select class="form-control col-md-1 col-sm-2 " id="scopeBeforeConn' + indexBeforeConn + '">' +
            '<option value="かつ" selected>かつ</option>' +
            '<option value="または">または</option>' +
            '</select>' +
            '<input type="text" class="form-control col-md-5 col-sm-4" id="tfBeforeScope' + indexBefore + '_op1">' +
            '<select class="form-control col-md-1 col-sm-1" id="tfBeforeScope' + indexBefore + '_op">' +
            '<option value=""></option>' +
            '<option selected value="==">=</option>' +
            '<option value="&gt">&gt</option>' +
            '<option value="&gt=">≧</option>' +
            '<option value="&lt">&lt</option>' +
            '<option value="&lt=">≦</option>' +
            '<option value="!=">≠</option>' +
            '<input type="text" class="form-control" id="tfBeforeScope' + indexBefore + '_op2">';
        '</div>';
        indexBefore++;
        indexBeforeConn++;
        // $("div.scopeBefore").append(destTag);
        $(this).parent().after(destTag);
        $("#" + divId).append($(this));
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
        // console.log("num property-group-" + "i: " + numSubProperty);
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
    console.log(numScopeAfter);
    if (numScopeAfter == 0) {
        var after = new Object();
        after.op1 = "";
        after.op2 = "";
        after.op = "";
        after.connector = "";
        afters.push(after);
    }
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

    //find before
    var numScopeBefore = $("div.scope-before").length;
    if (numScopeBefore == 0) {
        var before = new Object();
        before.op1 = "";
        before.op2 = "";
        before.op = "";
        before.connector = "";
        befores.push(before);
    }
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

    // console.log(templateJson);


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
            // console.log(retJson);
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
        '<span id="btnDownloadTemplate" hidden>download XML file</span>' +
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
                '<option value=""></option>' +
                '<option selected value="==">=</option>' +
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
                '<option value=""></option>' +
                '<option selected value="==">=</option>' +
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
    $("#patternSelection select").val("none");
}

$.addBtnHeaderListener = function() {
    $("#btnHeader").click(function() {
        $.resetGlobalVars();
        $.removeSelects();
        $("#scopeSelection select").val("global");
        $(".templateDiv").remove();
        $("#btnSubmitTemplate").remove();
        $("#btnDownloadTemplate").remove();
        $("#retDiv").remove();
    });
}

$.addBtnResetListener = function() {
    $("#btnReset").click(function() {
        $.resetGlobalVars();
        $.removeSelects();
        $("#scopeSelection select").val("global");
        $(".templateDiv").remove();
        $("#btnSubmitTemplate").remove();
        $("#btnDownloadTemplate").remove();
        $("#retDiv").remove();
    });
}
