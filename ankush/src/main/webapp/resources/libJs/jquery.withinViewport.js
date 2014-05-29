/**
* Within Viewport jQuery Plugin
*
* @description Companion plugin for withinViewport.js
* @author Craig Patik, http://patik.com/
* @version 0.2
* @date 2011-11-05
*/

;(function( $, window, undefined ) {
  
  /**
* $.withinViewport()
* @description jQuery method
* @param {Object} [settings] optional settings
* @return {Collection} Contains all elements that were within the viewport
*/
  $.fn.withinViewport = function(settings) {
    if (typeof settings === "string") { settings = {sides: settings}; }
    var opts = $.extend({}, settings, {sides: "all"}), elems = [];
    this.each(function() {
      if (withinViewport(this, opts)) {
        elems.push(this);
      }
    });
    return $(elems);
  };
  
  // Custom selector
  $.extend($.expr[":"], {
    "within-viewport": function(element) {
      return withinViewport(element, "all");
    }
  });
  
  /**
* Optional enhancements and shortcuts
*
* @description Uncomment or comment these pieces as they apply to your project and coding preferences
*/
  
  // Shorthand jQuery methods
  //
  $.fn.withinViewportTop = function(settings) {
    if (typeof settings === "string") { settings = {sides: settings}; }
    var opts = $.extend({}, settings, {sides: "top"}), elems = [];
    this.each(function() {
      if (withinViewport(this, opts)) {
        elems.push(this);
      }
    });
    return $(elems);
  };
  
  $.fn.withinViewportRight = function(settings) {
    if (typeof settings === "string") { settings = {sides: settings}; }
    var opts = $.extend({}, settings, {sides: "right"}), elems = [];
    this.each(function() {
      if (withinViewport(this, opts)) {
        elems.push(this);
      }
    });
    return $(elems);
  };
  
  $.fn.withinViewportBottom = function(settings) {
    if (typeof settings === "string") { settings = {sides: settings}; }
    var opts = $.extend({}, settings, {sides: "bottom"}), elems = [];
    this.each(function() {
      if (withinViewport(this, opts)) {
        elems.push(this);
      }
    });
    return $(elems);
  };
  
  $.fn.withinViewportLeft = function(settings) {
    if (typeof settings === "string") { settings = {sides: settings}; }
    var opts = $.extend({}, settings, {sides: "left"}), elems = [];
    this.each(function() {
      if (withinViewport(this, opts)) {
        elems.push(this);
      }
    });
    return $(elems);
  };
  
  // Custom jQuery selectors
  //
  $.extend($.expr[":"], {
    "within-viewport-top": function(element) {
      return withinViewport(element, "top");
    },
    "within-viewport-right": function(element) {
      return withinViewport(element, "right");
    },
    "within-viewport-bottom": function(element) {
      return withinViewport(element, "bottom");
    },
    "within-viewport-left": function(element) {
      return withinViewport(element, "left");
    }
    //,
    // "within-viewport-top-left-45": function(element) {
    // return withinViewport(element, {sides:'top left', top: 45, left: 45});
    // }
  });
  
})(jQuery, window);