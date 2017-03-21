describe("Strategy REST testing suite",function(){
  let strategyService;
  var userService;
  var http;

  beforeEach(angular.mock.module('RoboAdviceApp'));

  beforeEach(inject(function($httpBackend,_strategyService_,_userService_) {
    //_userService_.isValsecchi("ciao");
    strategyService = _strategyService_;
    userService = _userService_;
    http = $httpBackend;

  }));




})
