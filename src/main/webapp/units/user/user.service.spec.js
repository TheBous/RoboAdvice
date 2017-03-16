describe("User Test Suite",function(){
    var userService;
    // var http;

    var today = new Date();

    var pendingStrategy = new Strategy({name: "Strategy#1", date: { year: today.getUTCFullYear(), monthValue : today.getMonth(), dayOfMonth: today.getDate() },
        isActive: 1, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var activeStrategy = new Strategy({name: "Strategy#2", date: { year: 2017, monthValue : 3, dayOfMonth: 1 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var oldStrategy = new Strategy({name: "Strategy#3", date: { year: 2017, monthValue : 2, dayOfMonth: 28 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });

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
        userService.logged = true;
        userService.setStrategyHistory = [{}];
        userService.newStrategy = [{}];
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

    describe("User set his own strategy",function(){
        it("SETSTRATEGY || if object defined",function(){
            expect(userService.setStrategyHistory).toBeDefined();
        });
        it("SETSTRATEGY || return if array setStrategy())",function(){
            expect(userService.setStrategyHistory.constructor).toEqual(Array);
        });
        it("SETSTRATEGY || return type setStrategy())",function(){
            expect(typeof(userService.setStrategyHistory)).toEqual(typeof(pendingStrategy));
        });

    });

    describe("User do update",function(){
        it("UPDATE || user changed any fields)",function(){
            let updateUserObj =
                {
                    name: "j",
                    surname: "g",
                };
            userService.update(updateUserObj);
            expect((
                userService.userObj.name != updateUserObj.name ||
                userService.userObj.surname != updateUserObj.surname
            )).toEqual(false);
        });
    });

    describe("User create a new strategy",function(){
        it("NEWSTRATEGY || if object defined",function(){
            expect(userService.newStrategy).toBeDefined();
        });
        it("NEWSTRATEGY || return if array setStrategy())",function(){
            expect(userService.newStrategy.constructor).toEqual(Array);
        });
        it("NEWSTRATEGY || return type setStrategy())",function(){
            expect(typeof(userService.newStrategy)).toEqual(typeof(pendingStrategy));
        });
    });

    describe("User can logout",function(){
        it("LOGOUT || logout()",function(){
            expect(userService.userObj).toBeDefined();
        });
        it("LOGOUT || logout()",function(){
            expect(userService.userObj).toBeTruthy();
        });
        it("LOGOUT || logout())",function() {
            expect(userService.logout(userService.userObj)).toBeUndefined();
        });
        it("LOGOUT || logout())",function() {
            expect(userService.logout(userService.logged)).toBeFalsy();
        });
    });
});
