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
					callbackOpen : null,
					callbackClose : null,
					content : 'hide'
			};
			var o = $.extend(defaults,options);
			
			 return this.each(function(){
				 if(o.content == 'hide'){
					 $(this).children().first().next().hide(200,eval(o.callbackClose));
					 $(this).children().first().children().first().addClass('showPlusMinus'); 
				 }
				 else{
					 $(this).children().first().next().show(200,eval(o.callbackOpen));
					 $(this).children().first().children().first().removeClass('showPlusMinus');
				}
				 $(this).children().first().children().first().click(function(){
					 if($(this).parent().next().is(":visible")){
						 $(this).parent().next().hide(200,eval(o.callbackClose));
						 $(this).addClass('showPlusMinus');
					 }
					 else{
						 $(this).parent().next().show(200,eval(o.callbackOpen));
						 $(this).removeClass('showPlusMinus');
					 }
					 
				 });
		     });
		 } 
  	};

  $.fn.expandCollapse = function( method ) {
    if ( methods[method] ) {
      return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
    } else if ( typeof method === 'object' || ! method ) {
      return methods.init.apply( this, arguments );
    } else {
      $.error( 'Method ' +  method + ' does not exist on Expandable plugin' );
    }    
  
  };

})( jQuery );
