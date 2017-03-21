describe("Strategy Test Suite - ",function(){
  let strategyService;
  var userService;
  var http;

  var today = new Date();

  var pendingStrategy = new Strategy({name: "Strategy#1", date: { year: today.getUTCFullYear(), monthValue : today.getMonth()+1, dayOfMonth: today.getDate() },
    active: 1, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
  var activeStrategy = new Strategy({name: "Strategy#2", date: { year: 2017, monthValue : 3, dayOfMonth: 1 },
    active: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
  var oldStrategy = new Strategy({name: "Strategy#3", date: { year: 2017, monthValue : 2, dayOfMonth: 28 },
    active: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });

  beforeEach(angular.mock.module('RoboAdviceApp'));
  beforeEach(inject(function($httpBackend,_strategyService_,_userService_) {
    //_userService_.isValsecchi("ciao");
    strategyService = _strategyService_;
    userService = _userService_;
    http = $httpBackend;
    http.when('POST', '/strategy/delete').respond(200,{statusCode: 0});
    //http.expectPOST('/strategy/delete');

  }));

  /*
    USER LOGGED
  */

  describe("The user is logged and has 2 strategy, one of them is active",function(){
    var strategyService;
    beforeEach(inject(function(_strategyService_){
      strategyService = _strategyService_;
      strategyService.strategyHistory = [ oldStrategy,activeStrategy ];
    }));

    it("he wants to see his strategy history",function(){
      http.when('POST', '/strategy/getFullHistory').respond(200,{statusCode: 0,data:[
        oldStrategy,
        activeStrategy
      ]});
      strategyService.setHistory();
      http.flush();
      expect(strategyService.strategyHistory).toBeDefined();
      expect(strategyService.strategyHistory.length).toEqual(2);
    });

    it("he wants to see current strategy assets allocation", function(){
      expect(strategyService.getCurrentStrategy().getAssets()).toBeDefined();
    });

    it("he wants to see standard Strategies",function(){
      let standardStrategies = strategyService.getStandardStrategies();
      expect(strategyService.getStandardStrategies().length).toEqual(5);
      standardStrategies.forEach(function(strategy){
        expect(["Bonds","Income","Balanced","Growth","Stocks"].includes(strategy.name)).toBeTruthy();
      })
    })

    it("he wants to get the current strategy",function(){
      expect(strategyService.getCurrentStrategy()).toEqual(strategyService.strategyHistory[strategyService.strategyHistory.length-1]);
    });
    it("he wants to add a new strategy",function(){
      let oldStrategyHistoryLength = strategyService.strategyHistory.length;
      strategyService.newStrategy(pendingStrategy);
      expect(strategyService.strategyHistory.length).toEqual(oldStrategyHistoryLength+1);
      expect(strategyService.getCurrentStrategy()).toEqual(pendingStrategy);
    });

    it("he can delete the LAST pending strategy",function(){
      // add a new strategy that will be in pending
      let beforeInsertLength = strategyService.strategyHistory.length;
      strategyService.newStrategy(pendingStrategy.strategyObj);
      // expect(strategyService.getLastStrategy().isPending()).toBeTruthy();
      http.when('POST', '/strategy/delete').respond(200,{statusCode: 0});
      strategyService.deletePending();
      http.flush();
      expect(strategyService.strategyHistory.length).toEqual(beforeInsertLength);
    });

    it("he can't delete an active strategy", function(){
      let oldLength = strategyService.strategyHistory.length;
      // delete the pending strategy
      strategyService.deletePending();
      expect(strategyService.strategyHistory.length).toEqual(oldLength);
    })
  });

  /*
    NEW USER
  */

  describe("The user has just completed the pre wizard",function(){
    let strategyService;
    beforeEach(inject(function(_strategyService_){
      strategyService = _strategyService_;
      strategyService.strategyHistory = [ ];
    }));

    it("he can add the first strategy",function(){
      strategyService.newStrategy(pendingStrategy);

      expect(strategyService.getCurrentStrategy()).toEqual(pendingStrategy);
      expect(strategyService.getHistory().length).toEqual(1);
      let theStrategy = (strategyService.getHistory())[0];
      expect(theStrategy).toEqual(pendingStrategy);
      expect(theStrategy.isActive).toBeTruthy();
      expect(pendingStrategy.isPending()).toBeTruthy();
    });

    it("he can delete the first strategy and add a new one",function(){
      strategyService.newStrategy(pendingStrategy);
      // set the backend response
      http.when('POST', '/strategy/delete').respond(200,{statusCode: 0});

      strategyService.deletePending();
      http.flush();
      expect(strategyService.strategyHistory.length).toEqual(0);
    });

    it("he wants to see the initial amount that must be 10000",function(){
      strategyService.newStrategy(pendingStrategy);
      let theStrategy = strategyService.getHistory()[0];
      expect(theStrategy.getInitialAmount()).toEqual(10000)
    })
  });

  /*
    USER NOT LOGGED
  */

  describe("the user is not logged",function(){
    let strategyService;
    beforeEach(inject(function(_strategyService_){
      strategyService = _strategyService_;
      strategyService.strategyHistory = [ ];
    }));

    it("he wants to see his strategy but it's empty",function(){
      expect(strategyService.getHistory().length).toEqual(0);
    });
  });

// end test suite
});
