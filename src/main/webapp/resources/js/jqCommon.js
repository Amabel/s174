var graphTemppateFilePath;

$(document).ready(function() {
    $.generateFormPatternSelection();
});

$.generateFormPatternSelection = function() {
    $("#formPatternSelection").removeAttr("hidden");
    // load json and add divs
    $.getJSON("resources/common/patterns.json", function(data) {
        $.each(data.property_patterns, function(key, patterns) {
            // $.each(patterns, function(patternName, pattern) {
            //     var tfid = "tfPtn" + patternName;
            //     $("#formPatternSelection").append(
            //         '<div class="form-group">' +
            //         '<label for="' + tfid + '">' + pattern.name + '： </label><br>' +
            //         '<label>' + pattern.intent + '</label>' +
            //         '<input type="text" class="form-control" name="' + tfid + '" value="" id="' + tfid + '">' +
            //         '</div>'
            //     );
            // });

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
    // $("#" + selectId).after(
    //     '<div class="templateDiv" id="' + selectId + 'Template">' +
    //     template +
    //     '</div>'
    // );
    var reg = new RegExp("〇〇", "g");
    var replacedTemplate = template.replace(reg, ",property,");
    var templateArray = replacedTemplate.split(",");

    var tags = '<div class="templateDiv" id="' + selectId + 'Template">' +
        '<div class="propertyDiv input-group">';
    // var tags = '<div class="templateDiv input-group" id="templateDiv">';
    var propertyIndex = 0;
    for (var i = 0; i < templateArray.length; i++) {
        if (templateArray[i] == "property") {
            tags += '<input type="text" class="form-control" id="templateProperty' + propertyIndex + '" required>';
            propertyIndex++;
        } else if (templateArray[i] != "") {
            tags += '<span class="input-group-addon">' + templateArray[i] + '</span>';
        }
    }
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
        '<button type="button" class="btn btn-success btnAddScope" id="btnAddScope">Add scope</button>' +
        '</div>';
    $(".templateDiv").prepend(addScopeTag);
    $.addAddScopeListener();
}

$.addAddScopeListener = function() {
    $("#btnAddScope").click(function() {
        var afterScopeTag = '<div>' +
            '<div class="form-group" id="divAfterScpoe">' +
            '<label class="scopeLabel control-label" for="afterScope">After: </label>' +
            '<div>' +
            '<input type="text" class="form-control" id="afterScope">' +
            '</div></div></div>';
        var beforeScopeTag = '<div class="form-group" id="divBeforeScope">' +
            '<label for="beforeScope" class="control-label">Before: </label>' +
            '<div>' +
            '<input type="text" class="form-control" id="beforeScope">' +
            '</div></div>';
        $(".templateDiv").prepend(afterScopeTag);
        $(".templateDiv").append(beforeScopeTag);
        $("#btnAddScope").parent().remove();
    });
}

$.submitTemplate = function(patternName, numProperty) {
    console.log("pattern name: " + patternName + ", number: " + numProperty);
    // find properties
    var params = new Array();
    for (var i = 0; i < numProperty; i++) {
        console.log("param " + i + ": " + $("#templateProperty" + i).val());
        params[i] = $("#templateProperty" + i).val();
    }
    // new json
    var templateJson = new Object();
    templateJson.pattern = patternName;
    templateJson.params = params;

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
