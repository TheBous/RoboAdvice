describe("Portfolio Test Suite - ",function(){
  let portfolioService;
  //var userService;
  var http;

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

  var secondPortfolio = {
    totalAmount: 10000,
    date: {
      year:2017,
      monthValue:3,
      dayOfMonth: 20
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

  var thirdPortfolio = {
    totalAmount: 10000,
    date: {
      year:2017,
      monthValue:3,
      dayOfMonth: 21
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
  beforeEach(inject(function($httpBackend,_portfolioService_) {
    //_userService_.isValsecchi("ciao");
    portfolioService = _portfolioService_;
    http = $httpBackend;
  }));

  /*
    USER LOGGED WITH 1 portfolio
  */
  describe("The user has 1 portfolio",function(){
    it("and wants to see his portfolio history and amounts",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[
        firstPortfolio
      ]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.portfolioHistory.length).toEqual(1);
    });
    it("and get portfolio's timeline",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[
        firstPortfolio
      ]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.getPortfolioHistory().length).toEqual(1);
    });
    it("and can't see differences between today portfolio and yesterday",function(){

    });
  });

  /*
    USER WITH SOME PORTFOLIO
  */
  describe("The user has some portfolios",function(){
    it("and wants to see his portfolio history and amounts",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[
        firstPortfolio,
        secondPortfolio,
        thirdPortfolio
      ]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.portfolioHistory.length).toEqual(3);
    });
    it("and wants to get portfolio's timeline",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[
        firstPortfolio,
        secondPortfolio,
        thirdPortfolio
      ]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.getPortfolioHistory().length).toEqual(3);
    });
    it("and want to see differences between today portfolio and yesterday",function(){

    });
  });

  /*
    USER LOGGED FOR THE FIRST TIME, NO PORTFOLIOS
  */
  describe("The user doesn't have portfolios",function(){
    it("He can't see portfolio history",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.portfolioHistory.length).toEqual(0);
    });
    it("He can't see a timeline",function(){
      http.when('POST', '/portfolio/getFullHistory').respond(200,{statusCode: 0,data:[]});
      portfolioService.getFullHistory(function(){});
      http.flush();
      expect(portfolioService.getPortfolioHistory().length).toEqual(0);
    });
    it("He can't have a differences between two portfolios",function(){
      
    });
  });
});
