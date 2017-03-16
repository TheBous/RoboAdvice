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
        userService.setStrategyHistory = [{}]
    }));
    describe("User do login",function(){
        it("INIT || userObj => typeof(object)",function(){
            expect(typeof(userService.userObj)).toEqual('object');
        });
        it("INIT || userObj => Any object properties is only one",function(){
            expect(Object.keys(userService.userObj).length).toEqual(6);
        });
        //OTHER TESTS
    });
    describe("User do update",function(){
        it("UPDATE || user changed any fields)",function(){
            let updateUserObj =
                {
                    name: "j",
                    surname: "g",
                };
            expect((
                userService.userObj.name != updateUserObj.name ||
                userService.userObj.surname != updateUserObj.surname
            )).toEqual(false);
        });
    });

    describe("User set his own strategy",function(){
        it("SETSTRATEGY || if object defined",function(){
            expect(userService.setStrategyHistory).toBeDefined();
        });
        it("SETSTRATEGY || return if array setStrategy())",function(){
            expect(userService.setStrategyHistory.constructor).toEqual(Array);
        });
        it("SETSTRATEGY || return type setStrategy())",function(){
            expect(userService.setStrategyHistory).toEqual(jasmine.any(Object));
        });
    });
    describe("User create a new strategy",function(){
        it("NEWSTRATEGY || )",function(){
            var fakeStrategy = new Strategy("")
                {

                };
            expect(fakeStrategy).toEqual(Array);
        });
    });
});
