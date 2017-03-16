describe("User Test Suite",function(){
    var userService;
    // var http;
    beforeEach(angular.mock.module('RoboAdviceApp'));

    beforeEach(inject(function(_userService_, $httpBackend) {
        userService = _userService_;
        userService.userObj = {
            id: 1,
            email: "g@g.com",
            name: "j",
            surname: "g",
            password: 12345678,
            role: "USER"
        };
        

        /*http = $httpBackend;
        http.when("POST", "http://localhost:8080/users/login").respond(200,
            {
                id: 1,
                email: "g@g.com",
                name: "j",
                surname: "g",
                password: 12345678,
                role: "USER"
            }
        );*/


    }));
    describe("User do login",function(){
        it("INIT || userObj object define",function(){
            //make fake call

           /* userService.doLogin();
            http.flush();*/

           expect(typeof(userService.userObj)).toEqual('object');
        });

    });

});
