$(function(){

    const appendMatter = function(data){
        var matterCode = '<a href="#" class="matter-link" data-id="' +
            data.id + '">' + data.name + '</a><br>';
        $('#matter-list')
            .append('<div>' + matterCode + '</div>');
    };

    //Loading books on load page
    //$.get('/matters/', function(response)
    //{
    //    for(i in response) {
    //        appendBook(response[i]);
    //    }
    //});

    //Show adding matter form
    $('#show-add-matter-form').click(function(){
        $('#matter-form').css('display', 'flex');
    });

    //Closing adding matter form
    $('#matter-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Getting matter
    $(document).on('click', '.matter-link', function(){
        var link = $(this);
        var matterId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/todolist/' + matterId,
            success: function(response)
            {

               var code = '<span>Автор:' + response.author + '</span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Дело не найдено!');
                }
            }
        });
        return false;
    });

    //Adding matter
    $('#save-matter').click(function()
    {
        var data = $('#matter-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/todolist/',
            data: data,
            success: function(response)
            {
                $('#matter-form').css('display', 'none');
                var matter = {};
                matter.id = response;
                var dataArray = $('#matter-form form').serializeArray();
                for(i in dataArray) {
                    matter[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendMatter(matter);
            }
        });
        return false;
    });


    //Delete matter
        $('#delete-matter').click(function()
        {
            var data = $('#matter-delete form').serialize();
            $.ajax({
                method: "DELETE",
                url: '/todolist/',
                data: data,
                success: function(response)
                {
                    matterId.remove();
                }
            });
            return false;
        });
});