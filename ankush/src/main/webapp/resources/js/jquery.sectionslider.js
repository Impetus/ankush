/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

;(function( $ ){
    var methods = {
    	setTimeOutFlag : false,	
        init : function(options) {
        	var self = this;
        	var settings = {
       	    		'test' : 'test',
       	    		'time' : 1000,
       	    		'visibility' : function(){$('#dashboard-section').show();},
       	    };
            $(this).hide();
            
            if(options) {
              $.extend(settings, options);
            }
            var panels = new Array();
            $.data(document, "panels", panels);
            // first call add a root panel for preparing play ground
            methods.addRootPanel.call(this, settings);
        },
        //this method will load cluster overview page
        addRootPanel : function(settings){
            var panels = $.data(document, "panels");
            var type = {"name": "root", "zIndex" : 100};
            panels["root"] = type;
            $.data(document, "panels", panels);
            $('.section',this).width($(document).width());
            
                        
            $('.section',this).append('<div class="section-root"></div>');
            if(settings.method == 'get') {
                $.get(settings.url, settings.params, function(data){
                    $(".section-root", '.section').html(data);
                    settings.visibility();
                });
            }
            else {
                $.post(settings.url, settings.params, function(data){
                    $(".section-root", '.section').html(data);
                });
            }
            // add transition so that it can render via sliding
            methods.transition.call(this, settings, panels["root"]);
            if(typeof settings.callback == 'function'){
            	if(settings.callbackData != undefined)
            		settings.callback.call(this, settings.callbackData);
            	else{
            		settings.callback.call(this);}
            }
            $('title').html('Ankush:'+settings.title);
        },
      //this method will load cluster overview page
        addRootConfigPanel : function(){
          var rootSettings = {
        	       current : 'login-content',
        	       url : baseUrl + '/dashboardnew/home',
        	       method : 'get',
        	       visibility : function(){$('#dashboard-section').hide();},
        	       time : 1000,
        	       title : 'Cluster Overview',
        	       params : {}
             	};
          methods.init.call(this,rootSettings);
          var configSettings = {
       	       current : 'login-content',
       	       url : baseUrl + '/auth/config',
       	       method : 'get',
       	       title : 'Configuration',
       	       params : {}
            	};
          methods.addParentPanel.call(this,configSettings);
        },
        //this method will load parent page
        addParentPanel : function(settings) {
        	if($(".section-parent").length != 0){
            		$('.section-parent').hide();
            		$('.section-parent').remove();
            }
        	var panels = $.data(document, "panels");
            var type = {"name": "parent", "zIndex" : 101};
            panels["parent"] = type
            $.data(document, "panels", panels);
            $('.section', this).append('<div class="section-parent"></div>');
            methods.removeAllChildPanel.call(this,settings);
            methods.transition.call($('.section-parent'), settings, panels["parent"]);
            if(settings.method == 'get') {
                $.ajax({
                 	  type: 'GET',
                 	  'url': settings.url,
                 	  'async':true,
                 	  'success' : function(data) {
                 		  $(".section-parent", '.section').html(data);
                       },
                       error : function() {
                       }
                 	});
             }
             else {
                 $.post(settings.url, settings.params, function(data){
                     $(".section-parent", '.section').html(data);
                 });
             }
            if(typeof settings.callback == 'function'){
            	if(settings.callbackData != undefined)
            		settings.callback.call(this, settings.callbackData);
            	else
            		settings.callback.call(this);
            }
            $('title').html('Ankush:'+settings.title);
            
        },
        //this method will remove parent page
        removeParentPanel : function(settings) {
        	var panels = $.data(document, "panels");
        	if(undefined == panels.parent) {
                //alert('Parent doesn\'t exist.');
                return false;
            }
            $(".section-root").show();
            methods.reverseTransition.call($('.section-parent'), settings, 0, 'parent');
            if(typeof settings.callback == 'function'){
            	if(settings.callbackData != undefined)
            		settings.callback.call(this, settings.callbackData);
            	else
            		settings.callback.call(this);
            }
            $('title').html('Ankush:'+settings.title);   
        },
        //this method will remove single or multiple existing child
        removeChildPanel : function(settings) {
        	//alert(settings.childNo);
        	com.impetus.ankush.removeAutorefreshCall(settings.childNo);
        	if(settings.title == undefined){
        		settings.title = "";
        	}
        	var panels = $.data(document, "panels");
            var childCounter = 1;
            if(undefined != panels.children && panels.children.length > 0) {
                childCounter = panels.children.length + 1;
            }
            else {
                panels["children"] = new Array();
            }
            var panels = $.data(document, "panels");
            if(settings.childNo > childCounter) {
                alert('Child doesn\'t exist.');
                return false;
            }
            for(var i=settings.childNo; i < childCounter; i++) {
            	if(settings.previousCallBack == undefined){
            		settings.previousCallBack = "";
            	}
            	if(settings.previousCallBack != 'undefined'){
            		if(methods.setTimeOutFlag == false){
            			methods.setTimeOutFlag = true;	
	                	setTimeout(function()
			            {  for(var j=settings.childNo; j < childCounter; j++) {
		                		var callBacks = $('.nav-bar-'+j).attr('id').split('///');
		                        settings.previousCallBack = callBacks[1];
		                		eval(settings.previousCallBack);
			            	}
			            methods.setTimeOutFlag = false;
			            },80);
            		}
                }
                panels.children.pop();
                $.data(document, "panels", panels);
            	methods.reverseTransition.call($('.section-children-' + (i)), settings, panels.children.length+1, 'child');
            }
            if(settings.childNo != 1)
            	$('.section-body', $('.section-children-' + (settings.childNo-1))).show();
            else{
            	$('.section-parent').show();
            }
            if((childCounter > 2) && (settings.childNo != 1)){
            	var title = $('title').text().split(">"+settings.title);
	            var newTitle = title[0];   
	       	    $('title').html(newTitle);
            }
            else{ 
            	$('title').html('Ankush:Cluster Overview');
            	com.impetus.ankush.dashboard.createTile();
            	}
            methods.setBottomLine();
        },
        //This method will remove all existing child pages
        removeAllChildPanel : function(settings) {
        	var panels = $.data(document, "panels");
            var childCounter = 1;

            if(undefined != panels.children && panels.children.length > 0) {
                childCounter = panels.children.length + 1
            }
            else {
                panels["children"] = new Array();
            }
            var temp = panels.children;
            panels["children"] = new Array();
            $.data(document, "panels", panels);
            var childCounter = temp.length;
            if($('.section-parent'))
            	$('.section-parent').show();
            else 
            	$('.section-root').show();
            for(var i=0; i < childCounter; i++) {
            	methods.reverseTransition.call($('.section-children-' + (i+1)), settings, panels.children.length+1, 'child');
            }
            
        },
        //this method will load new child
        addChildPanel : function(settings) {
        	autoRefreshCallTestVariable = 0;
        	//$('.section-root').hide();
    		//$('.section-root').remove();
        	if(settings.title == undefined){
        		settings.title = "";
        	}
        	var panels = $.data(document, "panels");
            var childCounter = 0;
            if(undefined != panels.children && panels.children.length > 0) {
                childCounter = panels.children.length + 1;
            }
            else {
                childCounter = 1;
                panels["children"] = new Array();
                if($('.section-children', this).length == 0)
                    $('.section', this).append('<div class="section-children"></div>');
            }
            if(childCounter>=1)
    		{
    			$(".section-root").css('position', 'fixed');
    		}
            if(childCounter >= 8) {
                alert('You have reached to maximum limit of child pages.');
                return false;
            }
            var type = {"name": "children", "id":"children-" + childCounter, "zIndex" : (101 + childCounter)};
            panels["children"].push(type);
            $.data(document, "panels", panels);
            var getScrollPos = $(window).scrollTop();
            $('.section-children', this).append('<div class="section-children-' + childCounter + '"></div>');
            $(".section-children-" + childCounter, $('.section-children')).append('<div class="nav-bar nav-bar-' + childCounter + '" id="'+settings.title+'///'+settings.previousCallBack+'///'+getScrollPos+'" data-placement="mouse" data-original-title="'+settings.tooltipTitle+'" data-toggle="tooltip"></div>');
            if(settings.tooltipTitle != undefined){
            	$(".nav-bar-"+childCounter).popover();
            	
    	    }
            $('.nav-bar-' + childCounter).click(function(){
            	$('.section-children-'+(childCounter - 1)).css('z-index',100 + childCounter);
            	var mainId = $(".nav-bar-" + childCounter).attr('id');
           	    var titleId = mainId.split('///');
           	    var removeTitle = titleId[0];
           	    var tileFunction = titleId[1];
	        	 $(this).sectionSlider('removeChildPanel', {
	                'childNo' : childCounter,
	                'title' : removeTitle,
	                'previousCallBack' : tileFunction,
	                'scrollPos' : titleId[2]
	             });
            });
            if(settings.method == 'get') {
                $.get(settings.url, settings.params, function(data){
                    $(".section-children-" + childCounter, $('.section-children')).append(data);
                    if(typeof settings.callback == 'function'){
                    	if(settings.callbackData != undefined)
                    		settings.callback.call(this, settings.callbackData);
                    	else
                    		settings.callback.call(this);
                    }
                });
            }
            else {
                $.post(settings.url, settings.params, function(data){
                    $(".section-children-" + childCounter, $('.section-children')).append(data);
                    if(typeof settings.callback == 'function'){
                    	if(settings.callbackData != undefined)
                    		settings.callback.call(this, settings.callbackData);
                    	else
                    		settings.callback.call(this);
                    }
                });
            }
            if(childCounter == 1){
                methods.transition.call($('.section-children-' + childCounter), settings, panels["children"][childCounter-1], childCounter);
                $('title').html('Ankush:'+settings.title);
            }
            else{
                methods.transition.call($('.section-children-' + childCounter), settings, panels["children"][childCounter-1], childCounter);
                $('title').append('>'+settings.title); 
            }
        },
        // this method will remove pages with animation
        reverseTransition : function(settings, childCounter, type){
        		if($.data(document, "panels").children.length===0)
        		{
        			$(".section-root").css('position', 'absolute');
        		}
        	if(type == 'parent') {
                $('.section-header', $('.section-parent')).css('position', 'static');
                $('.section-header', $('.section-parent')).css('min-height', '37px');
                $('.section-header', $('.section-parent')).css('height', '37px');
                $(".section-parent").css('width',($(document).width()) + 'px');
                $(".section-parent").css('margin-left','0px');
                $(".section-parent").css('position', 'fixed');
                $(".section-root .section-header").css('width', '100%').css('width', '-=90px');
                //$('.section-header', $('.section-parent')).css('position', 'static');
                $('.section-body', $('.section-parent')).css('margin-top', '0px');
                setTimeout(function()
	            		{
                		  com.impetus.ankush.dashboard.createTile();
	            		},100);
                $("#showLoading").show();
                $(this).animate({
                    marginLeft : screen.width + 'px'
                }, 1000, function(){
                  $(this).empty();
  	              $(this).remove();
  	              delete $(this);
  	              $("#showLoading").hide();
              });
        	}
        	else {
        		$('.section-header', $('.section-children-' + (childCounter))).css('position', 'static');
                $('.section-header', $('.section-children-' + (childCounter))).css('left', (50 + 20*(childCounter-1)) + 'px');
                $('.section-header', $('.section-children-' + (childCounter))).css('height', '37px');
                $('.section-body', $('.section-children-' + (childCounter))).css('margin-top', '0px');
                $('.section-body', $('.section-children-' + (childCounter-1))).show();
               $("#showLoading").show();  
               $(this).animate({
                      marginLeft : screen.width + 'px',
                      opacity : '1',
                  }, 1000, function(){
                	  $(this).empty();
    	              $(this).remove();
    	              delete $(this);
    	             $("#showLoading").hide();
    	        });
               $(window).scrollTop(settings.scrollPos);
               
        	}
        },
        
        //this method will add pages with animation
        transition: function(settings, type, childCounter){
        switch(type.name) {
              case 'root' :
            	  var elem = $(this);
            	  $(this).css('display', 'block');
                  $('.section', $(this)).css('z-index', type.zIndex);
                  $('.section', $(this)).css('top','0px');
                  $('.section', $(this)).css('margin-left',$(document).width() + 'px');
                  $('.section', $(this)).css('width', ($(document).width()) + 'px');
                  $('.section', $(this)).css('position','absolute');
                  $('.section', $(this)).css('background-color', '#fff');
                  $('.section-body', $('.section-root')).css('margin-top', '10px');

                  $('.left-bar', $(this)).css('top', '0px');
                  $('.left-bar', $(this)).css('z-index', '120');
                  $('.left-bar', $(this)).css('margin-left','-50px');
                  
                  $('.section-header').css('top', '0');
                 // $('.head_body_container').css('margin-left', '20px');
                  
                  
                  $('.left-bar', $(this)).animate({
                      marginLeft : '0px'
                    }, settings.time, function(){
                  });
                  
                 
                  
                  $("#showLoading").show();
                  $('.section', $(this)).animate({
                      marginLeft : '0px'
                    }, settings.time, function(){
                    	$(document).css('background', 'none');
                    	$(this).css('z-index', type.zIndex);
                    	$(this).css('top','0px');
                        $(this).css('width', ($(document).width()-50) + 'px');
                        $(this).css('position','static');
                        $(this).css('background-color', '#fff');

                		$(".section-root").css('position','static');
                        $('#' + settings.current).hide();
                        //$('.section-header', $('.section-root')).css('position', 'fixed');
                        $('.section-header').css('position', 'fixed');
                     	$('.section-header', $('.section-root')).css('z-index', type.zIndex);
                     	$('.section-header', $('.section-root')).css('height', "auto");
                		$(".section-root").css('position','absolute');
                        $('.section-body', $('.section-root')).css('margin-top', '67px');
                        
                        $('.section-header').css('top', '0');
                       // $('.head_body_container').css('margin-left', '20px');
                        $(".section-root").css('width', '100%').css('width', '-=50px');
                        
                                           	
                        // resize1
                       $( window ).resize(function() { 
                         	$('.section').width($(document).width());
                         	$(".section-root").css('width', '100%').css('width', '-=50px');
                         	$(".section-root .section-header").css('width', '100%').css('width', '-=90px');
                         	$(".section .section-parent").css('width', '100%').css('width', '-=50px'); 	
                         	$(".section-parent .section-header").css('width', '100%').css('width', '-=90px');
                         	$(".section-parent").css('width', '100%').css('width', '-=50px'); 	
                         	
                         	//header height calculate start//
                         	var headerHgt = $(".section-header", $('.section-root')).height();
                         	$(".section-body", $('.section-root')).css("margin-top", headerHgt+10+"px");
                         	//header height calculate end//
                         	
                         	
                         	
                        });
                        
                        
                        $("#showLoading").hide();
                    });
              break;
              case 'parent' :
            	  //alert(123);
            	  $('html, body').scrollTop(0);
            	  $(document).scrollTop(0);
            	  $(window).scrollTop(0);
            	  var elem = $(this);
                  $(this).css('display', 'block');
                  $(this).css('z-index', type.zIndex);
                  $(this).css('top','0px');
                  $(this).css('margin-left',$(document).width() + 'px');
                  $(this).css('position','absolute');
                  $(this).css('top', '0px');
                  $(this).css('background-color', '#fff');
                  $('.section-body', $('.section-parent')).css('margin-top', '10px');
                  $("#showLoading").show();
                  $($(this)).animate({
                      marginLeft : '0px'
                    }, 1000, function(){
                    	elem.css('width', 'static');
                        $(this).css('position','static');
                        $('.section-header', $('.section-parent')).css('position', 'fixed');
                        $('.section-header', $('.section-parent')).css('z-index', type.zIndex);
                     	$('.section-header', $('.section-parent')).css('height', "auto");
                     	$(".section-parent").css('position','absolute');
                     	
                     	
                     	$('.section-body', $('.section-parent')).css('margin-top', '67px');
                     	$('.section-body', $('.section-children-' + (childCounter))).css('margin-top', '67px');
                     	$(".section-parent").css('width',(document.documentElement["offsetWidth"]-50) + 'px');
                        
                        $(".section-parent .section-header").css('width', '100%').css('width', '-=90px');
                                               
                        
                        $(".section-root").hide();
                        $("#showLoading").hide();
                    });
              break;
              case 'children' :
            	  $(this).css('display', 'block');
                  $(this).css('z-index', type.zIndex);
                  $(this).css('left',(50 + 20*(childCounter-1)) + 'px');
                  $(this).css('top','0px');
                  $(this).css('margin-left',screen.width + 'px');
                  $('.section-children').css('width', (screen.width-50) + 'px');
                  $('.section-children-' + childCounter).css('width', (screen.width-50) + 'px');
                  $(this).css('width', (screen.width-50-20) + 'px');
                  $(this).css('min-height', '100%');
                  $(this).css('position','absolute');
                  $(this).css('top', '0px');
                  $(this).css('background-color', '#fff');
                  $("#showLoading").show();
                  $(this).animate({
                          marginLeft : '0px'
                      }, 1000, function(){
                    	  if(childCounter == 1){
                    		  $('.section-parent').hide();
                    		  //$('.section-root').hide();
                    	  }
                    	  else {
                    		  $('.section-body', $('.section-children-' + (childCounter-1))).hide();
                    		  $('.section-children-'+(childCounter-1)).css('z-index','');
                    		  $('.nav-bar-' + (childCounter-1)).css('z-index',200 + childCounter);
                    		}
                    	  $('.section-header', $('.section-children-' + (childCounter))).css('position', 'fixed');
                          $('.section-header', $('.section-children-' + (childCounter))).css('padding-left', '0px');
                          $('.section-header', $('.section-children-' + (childCounter))).css('z-index', type.zIndex);
                          $('.section-header', $('.section-children-' + (childCounter))).css('left', (50 + 20*(childCounter-1)) + 'px');
                          
                          $('.section-header', $('.section-children-' + (childCounter))).css('top', '0');
                          $('.head_body_container').css('margin-left', (50 + 20*(childCounter-1)) + 'px');
                          
                          $('.section-header', $('.section-children-' + (childCounter))).css('height', '57px');
                          $('.section-body', $('.section-children-' + (childCounter))).css('margin-top', '67px');
                          $('.section-children-' + (childCounter)).css('margin-left',((childCounter)*20) + 'px');
                          $('.section-children-' + (childCounter)).css('left','0px');
                          $('.section-children-' + (childCounter)).css('width',(document.documentElement["offsetWidth"] - 70 - (childCounter-1)*20) + 'px');
                          $(this).css('margin-left', (50 + (childCounter-1)*20) + 'px');
                          
                          
                          methods.resetDesign();
                          
                        //header height calculate start//
                   		//var headerHgt = $(".section-header", $('.section-children-' + (childCounter))).height();
                   		//$(".section-body", $('.section-children-' + (childCounter))).css("margin-top", headerHgt+10+"px");
                   		//header height calculate end//
                          // resize2
                          
                         $( window ).resize(function() {
                        	 $('.section').width($(document).width());
                        	$('.section-children-' + (childCounter)).css('width',(document.documentElement["offsetWidth"] - 70 - (childCounter-1)*20) + 'px');
                         	$(".section-root").css('width', '100%').css('width', '-=50px');
                         	$(".section .section-parent").css('width', '100%').css('width', '-=90px')  	
                         	$(".section-parent .section-header").css('width', '100%').css('width', '-=90px');
                         	$(".section-parent").css('width', '100%').css('width', '-=50px'); 

                         	methods.setBottomLine();
                         	
                         	//header height calculate start//
                         		var headerHgt = $(".section-header", $('.section-children-' + (childCounter))).height();
                         		$(".section-body", $('.section-children-' + (childCounter))).css("margin-top", headerHgt+10+"px");
                         	//header height calculate end//
                         	
                         	
                            });
                         
                          methods.setBottomLine();
                          
                          
                          $("#showLoading").hide();
                          $('html, body').scrollTop(0);
                    	  $(document).scrollTop(0);
                    	  $(window).scrollTop(0);
                    	  if(autoRefreshCallTestVariable == 0){
                    		  var obj1 = {};
                    		  var autoRefreshArray = [];
                    		  obj1.varName = ''; 
                    		  obj1.callFunction = "";
                    	      obj1.time = 0;
                    		  autoRefreshArray.push(obj1);
                    		  com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
                    	  }
                  });
              break;
            }
        },
        setBottomLine : function(){ 
        	        	
            $(".section-root .section-header").css('width', '100%').css('width', '-=90px');
           	$(".section-children .section-header").css('width', '100%').css('width', '-=90px');
         	$(".section-children-1 .section-header").css('width', '100%').css('width', '-=110px');
         	$(".section-children-2 .section-header").css('width', '100%').css('width', '-=130px');
         	$(".section-children-3 .section-header").css('width', '100%').css('width', '-=150px');
         	$(".section-children-4 .section-header").css('width', '100%').css('width', '-=170px');
         	$(".section-children-5 .section-header").css('width', '100%').css('width', '-=190px');
         	$(".section-children-6 .section-header").css('width', '100%').css('width', '-=210px');
         	$(".section-children-7 .section-header").css('width', '100%').css('width', '-=230px');
         	$(".section-children-8 .section-header").css('width', '100%').css('width', '-=250px');
         	
         	$(".heatmap_tbl").css('width', '100%').css('width', '-=582px'); 
         	
         	
         	if($(".heatmap_tbl").width() < 650){
         		$(".heatmap_tbl").css('width', '100%');
         		$(".heatmap_block").css('margin-top', '15px');
         		
         	}else {
         		$(".heatmap_tbl").css('width', '100%').css('width', '-=582px');
         		$(".heatmap_block").css('margin-top', '0px');
            }
         	
         	//for shards block of oracle cluster start 
         	if($(".shardsblo").width() < 750){
         		//$(".shardsblo").addClass("shardsblo_resize");
         		$(".shardsblo").removeClass("span8").addClass("clearfix").addClass("span12");
         		         		         		
         	}else {
         		//$(".shardsblo").removeClass("shardsblo_resize"); 
         		var parentWidth = $(".shardsblo").parent().width();
         		var currEleWidth = parentWidth*(65.812)/100;
         		if(currEleWidth >= 750) {
         			$(".shardsblo").removeClass("span12").removeClass("clearfix").addClass("span8");
         		}
         	}
         	//for shards block of oracle cluster end      	
         	
        },
        resetDesign : function(){ 
        	
        },
        show : function(options) { 
        },
        hide : function() {},
        update : function( content ) {
        }
        
    };
   
    
    //Method will be called whenever section slider method called from javascript file and call appropriate method based on the parameter passed
    $.fn.sectionSlider = function(methodOrOptions) {
        if ( methods[methodOrOptions] ) {
            return methods[ methodOrOptions ].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof methodOrOptions === 'object' || ! methodOrOptions ) {
            // Default to "init"
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.sectionSlider' );
        }  

    };
    
    
  
//Login box in center start//
    
    /* use as handler for resize*/
    $(window).resize(adjustLayout);
    /* call function in ready handler*/
    $(document).ready(function(){
        
        var def = $.Deferred();
        var id = setInterval(function() {
    	   var ht = $('#login-panel').outerHeight();
    	   if(ht !== 0){
    		  clearInterval(id);
    		  def.resolve();
    	   }
        }, 10);
        
        def.done(function(){
        	adjustLayout();
        });
       
    })

    function adjustLayout(){
    	
    	var top=($(window).height() - $('#login-panel').outerHeight())/2;
    	
    	if(top<0)
    		top=0;
        $('#login-panel').css({
            position:'relative',
            //left: ($(window).width() - $('#login-panel').outerWidth())/2,
            
            top: top
        });
        
        var ele = $("#login-content");
        if(ele[0].scrollHeight > ele.height()){
        	$(".footer_div").css("position", "relative");
        }else {
        	$(".footer_div").css("position", "absolute");
        }
        
        

    }
  //Login box in center end// 
   
    
})( jQuery );
