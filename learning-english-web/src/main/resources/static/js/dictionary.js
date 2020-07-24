$('document').ready(function() {

$('.table #deleteButton').on('click',function(event){
	event.preventDefault();
	var href = $(this).attr('href');
	$('#delRef').attr('href', href);
	$('#deleteLemmaModal').modal();
});

});

