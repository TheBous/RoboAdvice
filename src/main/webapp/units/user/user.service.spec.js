describe("User Test Suite",function(){

    var userService;
    var strategyService;
    var http;

    var today = new Date();

    var pendingStrategy = new Strategy({name: "Strategy#1", date: { year: today.getUTCFullYear(), monthValue : today.getMonth(), dayOfMonth: today.getDate() },
        isActive: 1, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var activeStrategy = new Strategy({name: "Strategy#2", date: { year: 2017, monthValue : 3, dayOfMonth: 1 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
    var oldStrategy = new Strategy({name: "Strategy#3", date: { year: 2017, monthValue : 2, dayOfMonth: 28 },
        isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });

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

    var firstPortfolio = {
      totalAmount: 10000,
      date: {
        year:2017,
        monthValue:3,
        dayOfMonth: 19
      },
      bondsAmount: 5000,
      bondsPercentage: 95,
      commoditiesAmount: 10000,
      commoditiesPercentage: 0,
      forexAmount: 10000,
      forexPercentage: 0,
      stocksAmount: 10000,
      stocksPercentage: 5
    };
    
    beforeEach(angular.mock.module('RoboAdviceApp'));

    beforeEach(inject(function(_userService_, $httpBackend) {
        userService = _userService_;
        http = $httpBackend;
    }));
    describe("User do login",function(){


        it("INIT || userObj => typeof(object)",function(){
            expect(typeof user).toEqual('object');
        });
        it("INIT || userObj => Any object properties is only one",function(){
            expect(Object.keys(user).length).toEqual(6);
        });
        // initialize user
        it("INIT || if user log in, logged => true",function(){
            userService.init(user);
            expect(userService.logged).toBeTruthy() && expect(userService.userObj).toEqual(user);
        });
        it("INIT || test rest login call",function(){
            http.when('POST', '/user/login').respond(200,{statusCode: 0, data: {token: 'token'}});
            userService.doLogin(userService.userObj);
            http.flush();
            expect(userService.userObj).toBeDefined();
        });

        //OTHER TESTS
    });


    describe("User set his own strategy",function(){

        it("SETSTRATEGY || if object defined",function(){
            expect(userService.setStrategyHistory).toBeDefined();
        });

        it("SETSTRATEGY || return type setStrategy())",function(){
          http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[
            firstPortfolio
          ]});
            http.when('POST', '/strategy/getFullHistory').respond(200,{statusCode: 0,data:[
                oldStrategy,
                activeStrategy
            ]});
            userService.setStrategyHistory();
            http.flush();
            //expect().toBeDefined();
            // expect(strategyService.strategyHistory.length).toEqual(2);
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
        it("UPDATE || update rest call)",function(){
            http.when('POST', '/user/update').respond(200,{statusCode: 0});
            userService.userObj = user;
            userService.update(updateUser, function(){});
            http.flush();
            expect(userService.userObj).toEqual(updateUser);
        });

    });

    describe("User create a new strategy",function(){
        it("NEWSTRATEGY || if object defined",function(){
            expect(userService.newStrategy).toBeDefined();
        });

    });

    describe("User can logout",function(){
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
