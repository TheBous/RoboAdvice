describe("User Test Suite",function(){
    var userService;
    var strategyService;
    var portfolioService;
    // var http;

    var today = new Date();

    var pendingStrategy = new Strategy({name: "Strategy#1", date: { year: today.getUTCFullYear(), monthValue : today.getMonth(), dayOfMonth: today.getDate() },
        isActive: 1, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var activeStrategy = new Strategy({name: "Strategy#2", date: { year: 2017, monthValue : 3, dayOfMonth: 1 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var oldStrategy = new Strategy({name: "Strategy#3", date: { year: 2017, monthValue : 2, dayOfMonth: 28 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });

    var fakePortfolio = new Portfolio({totalAmount: 23, date: { year: today.getUTCFullYear(), monthValue : today.getMonth(), dayOfMonth: today.getDate()},
        bondsAmount: 2000, bondsPercentage: 20, commoditiesAmount: 2000, commoditiesPercentage: 20, forexAmount: 4000, forexPercentage: 40,
        stocksAmount: 2000, stocksPercentage: 20});

    var user = {
        id: 1,
        email: "g@g.com",
        name: "j",
        surname: "g",
        password: 12345678,
        role: "USER"
    };

    var updateUser = {
        id: 1,
        email: "g@g.com",
        name: "h",
        surname: "g",
        password: 12345678,
        role: "USER"
    };
    beforeEach(angular.mock.module('RoboAdviceApp'));

    beforeEach(inject(function(_userService_, $httpBackend) {
        userService = _userService_;
        userService.logged = false;
        userService.setStrategyHistory = [{}];
        userService.newStrategy = [{}];

    }));
    describe("User do login",function(){

        beforeEach(inject(function(_userService_, _strategyService_){
            userService = _userService_;
            strategyService = _strategyService_;
            userService.setStrategyHistory = function(){};
        }));

        it("INIT || userObj => typeof(object)",function(){
            expect(typeof user).toEqual('object');
        });
        it("INIT || userObj => Any object properties is only one",function(){
            expect(Object.keys(user).length).toEqual(6);
        });
        // initialize user

        it("INIT || if user log in, logged => true",function(){
            userService.init(user);
            expect(userService.logged).toBeTruthy();
        });
        //OTHER TESTS
    });


    describe("User set his own strategy",function(){
        it("SETSTRATEGY || if object defined",function(){
            expect(userService.setStrategyHistory).toBeDefined();
        });
        it("SETSTRATEGY || return if setStrategy() return array)",function(){
            expect(userService.setStrategyHistory.constructor).toEqual(Array);
        });
        it("SETSTRATEGY || return type setStrategy())",function(){
            expect(typeof(userService.setStrategyHistory)).toEqual(typeof(pendingStrategy));
        });

    });


    describe("User do update",function(){
        it("UPDATE || user changed any fields)",function(){
            userService.userObj = user;
            expect((
                user.name != updateUser.name ||
                user.surname != updateUser.surname
            )).toEqual(true);
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
        beforeEach(inject(function(_userService_){
            userService = _userService_;
        }));
        it("LOGOUT || logout()",function(){
            expect(userService.userObj).toBeDefined();
        });
        it("LOGOUT || logout => user undefined)",function() {
            expect(userService.logout(userService.userObj)).toBeUndefined();
        });
        it("LOGOUT || logout => logged = false)",function() {
            expect(userService.logout(userService.logged)).toBeFalsy();
        });
    });
    describe("User can logout",function(){
        beforeEach(inject(function(_userService_){
            userService = _userService_;
        }));
        it("LOGOUT || logout()",function(){
            expect(userService.userObj).toBeDefined();
        });
        it("LOGOUT || logout => user undefined)",function() {
            expect(userService.logout(userService.userObj)).toBeUndefined();
        });
        it("LOGOUT || logout => logged = false)",function() {
            expect(userService.logout(userService.logged)).toBeFalsy();
        });
    });
});
