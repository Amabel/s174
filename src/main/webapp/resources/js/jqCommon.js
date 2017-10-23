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

    var tags = '<div class="templateDiv input-group" id="' + selectId + 'Template">';
    var propertyIndex = 0;
    for (var i = 0; i < templateArray.length; i++) {
        if (templateArray[i] == "property") {
            tags += '<input type="text" class="form-control" id="templateProperty' + propertyIndex + '">';
            propertyIndex++;
        } else if (templateArray[i] != "") {
            tags += '<span class="input-group-addon">' + templateArray[i] + '</span>';
        }
    }
    tags += '</div>';
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
            console.log(data);
            graphTemppateFilePath = data;
            $.showDownloadButton();
        },
        error: function(data) {
            alert('failed');
        }
    });

}

$.showDownloadButton = function() {
    // remove previous download mark
    $("#btnDownloadTemplate").remove();
    var buttonTag = '<div align="center">' +
        '<img src="resources/images/download-button.png" class="btn form-control" id="btnDownloadTemplate" alt="Download graph template" />' +
        '</div>';

    // append to submit button
    $("#btnSubmitTemplate").parent().after(buttonTag);
    $.addDownloadTemplateButtonListener2();
}

// $.addDownloadTemplateButtonListener1 = function() {
//     $("#btnDownloadTemplate").click(function() {
//         var url = "/s174/download/graph-templates/" + graphTemppateFilePath;
//         console.log(url);
//         $.fileDownload(url, {
//             successCallback: function(url) {
//
//                 alert('You just got a file download dialog or ribbon for this URL :' + url);
//             },
//             failCallback: function(html, url) {
//
//                 alert('Your file download just failed for this URL:' + url + '\r\n' +
//                     'Here was the resulting error HTML: \r\n' + html
//                 );
//             }
//         });
//     });
// }

$.addDownloadTemplateButtonListener2 = function() {
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
