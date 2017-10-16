$(document).ready(function() {
    // $("div").fadeIn(3000);
    $("#submitRequirement").click(function() {
        $("form").fadeOut();
    })
});

$.submitRequirement = function() {
    $("#formRequirement").submit(function(event) {
        var requirementJSON = $(this).serializeArray();
        console.log(requirementJSON);
        event.preventDefault();
        $.ajax({
            type: 'post',
            url: '/s173/requirement',
            dataType: 'JSON',
            data: {
                requirementJSON: JSON.stringify(requirementJSON)
            },
            success: function(data) {
                alert(data);
            },
            error: function(data) {
                alert('fail');
            }
        });
    });
}
