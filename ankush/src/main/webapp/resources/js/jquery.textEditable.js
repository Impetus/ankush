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

(function( $ ){
	var methods = { 
		init : function(options){
			var defaults = {
				title : '',
				cssClass : '',
				parentClass : '',
				cssStyle : 'height:15px;',
				type : 'text',							
				onSaveCallBack : null,
				onEscapeCallBack : null,
				onFocusOutCallBack : null,
				placeholder:''
			};
			var o = $.extend(defaults,options);
			var flag = true;
			 return this.each(function(){
				$(this).click(function recurse(){
					if(flag != false){
						var oldText = $(this).text();
						$(this).unbind('click');
						var divStyle = $(this).attr('style');
						$(this).removeAttr('style');
						$(this).addClass(o.parentClass);
						$(this).html('<input type="'+o.type+'" class="'+o.cssClass+'" title = "'+o.title+'" value="'+
								oldText+'" style="'+o.cssStyle+'" placeholder="'+o.placeholder+'"></input>');
						$(this).children('input').focus();
						$(this).children('input').on('focusout', function onOutFocus() {
							if(null != o.onFocusOutCallBack)
									flag = o.onFocusOutCallBack($(this).parent().attr('id'));
								if(flag){							
									var saveElement = $(this).parent();
									var saveValue = $(this).val(); 
									if(saveValue.length == 0)
										saveValue = 'Empty';
									$(this).parent().removeClass(o.parentClass);
									$(this).parent().attr('style',divStyle);
									$(this).parent().text(saveValue);
									saveElement.bind('click',recurse);
								}
						}).on('keydown', function (e) {
							if(e.which == 13){
								$(this).off('focusout');
								if(null != o.onSaveCallBack)
									flag = o.onSaveCallBack($(this).parent().attr('id'));
								
								if(flag){							
									var saveElement = $(this).parent();
									var saveValue = $(this).val();
									if(saveValue.length == 0)
										saveValue = 'Empty';
									$(this).parent().removeClass(o.parentClass);
									$(this).parent().attr('style',divStyle);
									$(this).parent().text(saveValue);
									saveElement.bind('click',recurse);
								}
								
							}
							else if(e.which == 27){
								$(this).off('focusout');
								if(null != o.onEscapeCallBack)
									flag = o.onEscapeCallBack($(this).parent().attr('id'));
								if(flag){
									var saveElement = $(this).parent();
									$(this).parent().removeClass(o.parentClass);
									$(this).parent().attr('style',divStyle);
									$(this).parent().text(oldText);
									saveElement.bind('click',recurse);
								}
								
							}
						});
					}
				
				});

			});
	  	}
    
  	};

  $.fn.textEditable = function( method ) {
    if ( methods[method] ) {
      return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
    } else if ( typeof method === 'object' || ! method ) {
      return methods.init.apply( this, arguments );
    } else {
      $.error( 'Method ' +  method + ' does not exist on Text editable plugin' );
    }    
  
  };

})( jQuery );
