describe("Strategy Test Suite - ",function(){
  var strategyService;
  var userService;
  var http;

  var today = new Date();

  var pendingStrategy = new Strategy({name: "Strategy#1", date: { year: today.getUTCFullYear(), monthValue : today.getMonth(), dayOfMonth: today.getDate() },
    isActive: 1, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
  var activeStrategy = new Strategy({name: "Strategy#2", date: { year: 2017, monthValue : 3, dayOfMonth: 1 },
    isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });
  var oldStrategy = new Strategy({name: "Strategy#3", date: { year: 2017, monthValue : 2, dayOfMonth: 28 },
    isActive: 0, bonds_p: 95, stocks_p: 0, forex_p: 0, commodities_p: 5 });

  beforeEach(angular.mock.module('RoboAdviceApp'));
  beforeEach(inject(function($httpBackend,_strategyService_,_userService_) {
    //_userService_.isValsecchi("ciao");
    strategyService = _strategyService_;
    userService = _userService_;
  }));

  describe("The user is logged and has 2 strategy, one of them is active",function(){
    var strategyService;
    beforeEach(inject(function(_strategyService_){
      strategyService = _strategyService_;
      strategyService.strategyHistory = [ oldStrategy,activeStrategy ];
    }));

    it("he want to see his strategy history",function(){
      expect(strategyService.strategyHistory).toBeDefined();
    });
    it("he want to get the current strategy",function(){
      expect(strategyService.getCurrentStrategy()).toEqual(strategyService.strategyHistory[strategyService.strategyHistory.length-1]);
    });
    it("he want to add a new strategy",function(){
      let oldStrategyHistoryLength = strategyService.strategyHistory.length;
      strategyService.newStrategy(pendingStrategy);
      expect(strategyService.strategyHistory.length).toEqual(oldStrategyHistoryLength+1);
      expect(strategyService.getCurrentStrategy()).toEqual(pendingStrategy);
    });

    it("he can delete the LAST pending strategy",function(){
      // add a new strategy that will be in pending
      strategyService.newStrategy(pendingStrategy);
      let beforeDeleteLength = strategyService.strategyHistory.length;
      strategyService.deletePending();
      expect(strategyService.strategyHistory.length).toEqual(beforeDeleteLength-1);
    });

    it("he can't delete an active strategy", function(){
      let oldLength = strategyService.strategyHistory.length;
      // delete the pending strategy
      strategyService.deletePending();
      expect(strategyService.strategyHistory.length).toEqual(oldLength);
    })
  });

  describe("The user have just completed the pre wizard",function(){
    var strategyService;
    beforeEach(inject(function(_strategyService_){
      strategyService = _strategyService_;
      strategyService.strategyHistory = [ pendingStrategy ];
    }));


  });

});
