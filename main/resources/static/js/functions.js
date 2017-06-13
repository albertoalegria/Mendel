$(document).ready(function() {
    //get groups
    var id = parseInt($(this).find('option:selected').attr("career-id"), 10);
    getGroups(id);
    $("#careers").change(function(){
        id = parseInt($(this).find('option:selected').attr("career-id"), 10);
        getGroups(id);
    });
    v1 = $("#checkIn").find(":selected").attr("value");
    v2 = $("#checkOut").find(":selected").attr("value");
           console.log("vals: " + v1 + ", " + v2);
           checkValues(v1, v2);

    //check lab hours
    hideLabHours();

    $("#yes").change(function() {
        $("#lab").slideDown();
        //$("#labHoursSelect").slideDown();
    });

    $("#no").change(function() {
        $("#lab").slideUp();
        //$("#labHoursSelect").slideUp();
        $("#labHours").val(0);
        //$('option:selected', this).removeAttr("selected");
        //$("#labSelect").val([]);
        $("#labSelect").val('').trigger('chosen:updated');
    });

    $("#checkIn").change(function() {
        //v1 = $("#checkIn").find(":selected").text();
        //v2 = $("#checkOut").find(":selected").text();
        v1 = $("#checkIn").find(":selected").attr("value");
        v2 = $("#checkOut").find(":selected").attr("value");
        checkValues(v1, v2);
    });

    $("#checkOut").change(function() {
        v1 = $("#checkIn").find(":selected").attr("value");
        v2 = $("#checkOut").find(":selected").attr("value");
        checkValues(v1, v2);
    });


    /*if($("#courses").val() == "") {
        $("#inputBtn").attr("disabled", true);
    }

    if($("#courses").val() != "") {
            $("#inputBtn").attr("disabled", false);
        }*/

    if($("#classrooms").val() == "") {
        $("#inputBtn").attr("disabled", true);
    }

    if($("#classrooms").val() != "") {
            $("#inputBtn").attr("disabled", false);
        }



    /*$("#courses").change(function() {
                //$("#inputBtn").attr("disabled", false);
                checkCourses();
    });*/

    $("#classrooms").change(function() {
                    //$("#inputBtn").attr("disabled", false);
                    checkClassrooms();
        });

    $(".chosen-select").chosen({ search_contains: true });
});


function checkClassrooms() {
 if($("#classrooms").val() == "") {
        $("#inputBtn").attr("disabled", true);
    }

    if($("#classrooms").val() != "") {
            $("#inputBtn").attr("disabled", false);
        }
}

function checkCourses() {
    if($("#courses").val() == "") {
            $("#inputBtn").attr("disabled", true);
        }
     if($("#courses").val() != ""){
                    $("#inputBtn").attr("disabled", false);
        }
}

function hideLabHours() {
    $("#lab").slideUp();
    //$("#labHoursSelect").slideUp();
}

function getGroups(id) {
    var url = "/courses/groups?id_career=" + id;// '/courses/groups?id_career=' + id;
    console.log(id);
    console.log(url);
    $("#groupsContent").load(url);
}

function checkValues(v1, v2) {
    v1 = parseInt(v1, 10);
    v2 = parseInt(v2, 10);

    console.log("values: " + v1 + ", " + v2);
    if(v1 < v2) {
        inputButton(true);
    } else {
        inputButton(false);
    }
}

function inputButton(ok) {
    if(ok) {
        $("#inputBtn").attr("disabled", false);
        $("#message").slideUp();
    } else {
        $("#inputBtn").attr("disabled", true);
        $("#message").slideDown();
    }
}
