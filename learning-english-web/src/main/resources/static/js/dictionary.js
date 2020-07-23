$('document').ready(function() {

$('.table #deleteButton').on('click',function(event){
    console.log("delete button click")
	event.preventDefault();
	var href = $(this).attr('href');
	$('#delRef').attr('href', href);
	$('#deleteLemmaModal').modal();
});

});

