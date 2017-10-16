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
    console.log(replacedTemplate);
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
    console.log(tags);
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
    console.log(JSON.stringify(templateJson));

    // send json
    $.ajax({
        type: 'post',
        url: '/s173/template',
        dataType: 'JSON',
        data: {
            templateJson: JSON.stringify(templateJson)
        },
        success: function(data) {
            alert(data);
        },
        error: function(data) {
            alert('fail');
        }
    });

}
