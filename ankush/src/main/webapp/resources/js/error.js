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

$(document).ready(function(){
	var collapsedSize = '18px';
	var baseUrlImages = baseUrl+"/public/images";
	var intIdSetHt = setInterval(function(){
		
		if($('.errorLineDiv').length > 0) {
			setErrorMsgHeight();
			
			$(document).live('mousemove',function(){
				if($('.errorLineDiv').attr('ui-dev') == "done"){
					return false;
				} else {
					$('.errorLineDiv').attr('ui-dev','done');
					setErrorMsgHeight();
				}
			});
			
			$(window).resize(function(){
				setTimeout(function(){
					if($('.errorLineDiv').height() > 20){
						var newHt = $('.errorLineDiv').find('a').first().outerHeight()+15;  //added 15 for padding etc.
						$('.errorLineDiv').height(newHt);
					}
				},500);
			});
			
			clearInterval(intIdSetHt);
		}
	}, 1000);
	
	
	
	
	function setErrorMsgHeight() {
		$('.errorLineDiv').each(function() {
		    var h = this.scrollHeight;
		    var div = $(this);
			
		    if (h > 40) {
		        div.css('height', collapsedSize);
		        div.append('<a class="arrow-link collapsed" href="#"><img class="collapsed" src="'+baseUrlImages+'/open_arrow.png" width="12" height="9" /></a>');
		        var link = div.find("img");
		        link.click(function(e) {
		        	if (link.hasClass('collapsed')) {
					    link.removeClass('collapsed');
		                //link.html('&#8593;');
						link.attr("src", baseUrlImages+"/close_arrow.png");
						
						div.animate({
							'height': (div.find('a').first().outerHeight()+15)
		                });
						
		            } else {
						link.addClass('collapsed');
						link.attr("src", baseUrlImages+"/open_arrow.png");
		                div.animate({
		                    'height': collapsedSize
		                });                
		            }
	
		        });
		    }
	
		});
	}
});
