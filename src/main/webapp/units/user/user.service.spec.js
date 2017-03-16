describe("User Test Suite",function(){
  var userService;
  beforeEach(angular.mock.module('RoboAdviceApp'));

  beforeEach(inject(function(_userService_) {
    //_userService_.isValsecchi("ciao");
    userService = _userService_;
  }));

  describe("User do login",function(){
    it("bho",function(){
        expect(userService.isValsecchi("zik")).toEqual(true);
    });
  });

});
