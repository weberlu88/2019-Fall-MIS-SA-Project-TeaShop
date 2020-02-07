(function(a) {
    var h = {
        Android: function() {
            return navigator.userAgent.match(/Android/i)
        },
        BlackBerry: function() {
            return navigator.userAgent.match(/BlackBerry/i)
        },
        iOS: function() {
            return navigator.userAgent.match(/iPhone|iPad|iPod/i)
        },
        Opera: function() {
            return navigator.userAgent.match(/Opera Mini/i)
        },
        Windows: function() {
            return navigator.userAgent.match(/IEMobile/i)
        },
        any: function() {
            return (h.Android() || h.BlackBerry() || h.iOS() || h.Opera() || h.Windows())
        }
    };

    function n() {
        a(".bg--parallax").each(function() {
            var x = a(this),
                z = "50%",
                y = a(window).height();
            if (h.any()) {
                a(this).css("background-attachment", "scroll")
            } else {
                a(window).scroll(function() {
                    var A = a(window).scrollTop(),
                        C = x.offset().top,
                        B = x.outerHeight();
                    if (C + B < A || C > A + y) {
                        return
                    }
                    x.css("backgroundPosition", z + " " + Math.round((C - A) * 0.2) + "px")
                })
            }
        })
    }

    function b() {
        var x = a("[data-background]");
        x.each(function() {
            if (a(this).attr("data-background")) {
                var y = a(this).attr("data-background");
                a(this).css({
                    background: "url(" + y + ")"
                })
            }
        })
    }

    function k() {
        var x = a(".menu-toggle");
        var y = a(".menu");
        x.on("click", function(z) {
            z.preventDefault();
            var A = a(this);
            if (!A.hasClass("active")) {
                A.addClass("active");
                y.slideDown(350)
            } else {
                A.removeClass("active");
                y.slideUp(350);
                y.find(".sub-menu").slideUp(350)
            }
        })
    }

    function v() {
        var x = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
        if (x == false) {
            a("body").on("click", ".menu--mobile .menu-item-has-children > .sub-toggle", function(z) {
                z.preventDefault();
                var y = a(this).parent(".menu-item-has-children");
                y.children(".sub-menu").slideToggle(350);
                y.siblings().find(".sub-menu").slideUp(350)
            })
        } else {
            a("body").on("touchstart", ".menu--mobile .menu-item-has-children > .sub-toggle", function(z) {
                z.preventDefault();
                var y = a(this).parent(".menu-item-has-children");
                y.children(".sub-menu").slideToggle(350);
                y.siblings().find(".sub-menu").slideUp(350)
            })
        }
    }

    function q() {
        var y = a(".header"),
            z = a(".header--2"),
            A = a(".header--3"),
            x = 1200,
            B = a(window).innerWidth();
        if (x > B) {
            a(".menu--mobile").hide();
            a(".menu-toggle").removeClass("active");
            if (z.length > 0) {
                z.find(".menu").addClass("menu--mobile");
                y.find(".ps-logo").appendTo(z.find(".navigation"))
            }
            if (A.length > 0) {
                A.find(".menu").addClass("menu--mobile")
            }
        } else {
            a(".menu").show();
            if (z.length > 0) {
                z.find(".menu").removeClass("menu--mobile");
                y.find(".ps-logo").appendTo(z.find(".center"))
            }
            if (A.length > 0) {
                A.find(".menu").removeClass("menu--mobile")
            }
        }
    }

    function u() {
        var y = a(".header"),
            z = 0,
            x = 300;
        if (y.data("sticky") === true) {
            a(window).scroll(function() {
                var A = a(this).scrollTop();
                if (A < z) {
                    if (A === 0) {
                        y.removeClass("navigation--sticky navigation--unpin navigation--pin")
                    } else {
                        if (A > x) {
                            y.removeClass("navigation--unpin").addClass("navigation--sticky navigation--pin")
                        }
                    }
                } else {
                    if (A > x) {
                        y.addClass("navigation--sticky navigation--unpin").removeClass("navigation--pin")
                    }
                }
                z = A
            })
        } else {
            return false
        }
    }

    function m() {
        var x = a(".owl-slider");
        if (x.length > 0) {
            x.each(function() {
                var P = a(this),
                    A = P.data("owl-auto"),
                    I = P.data("owl-loop"),
                    N = P.data("owl-speed"),
                    D = P.data("owl-gap"),
                    K = P.data("owl-nav"),
                    C = P.data("owl-dots"),
                    y = (P.data("owl-animate-in")) ? P.data("owl-animate-in") : "",
                    z = (P.data("owl-animate-out")) ? P.data("owl-animate-out") : "",
                    B = P.data("owl-item"),
                    H = P.data("owl-item-xs"),
                    G = P.data("owl-item-sm"),
                    F = P.data("owl-item-md"),
                    E = P.data("owl-item-lg"),
                    L = (P.data("owl-nav-left")) ? P.data("owl-nav-left") : "<i class='fa fa-angle-left'></i>",
                    M = (P.data("owl-nav-right")) ? P.data("owl-nav-right") : "<i class='fa fa-angle-right'></i>",
                    O = P.data("owl-duration"),
                    J = (P.data("owl-mousedrag") == "on") ? true : false;
                P.owlCarousel({
                    animateIn: y,
                    animateOut: z,
                    margin: D,
                    autoplay: A,
                    autoplayTimeout: N,
                    autoplayHoverPause: true,
                    loop: I,
                    nav: K,
                    mouseDrag: J,
                    touchDrag: true,
                    autoplaySpeed: O,
                    navSpeed: O,
                    dotsSpeed: O,
                    dragEndSpeed: O,
                    navText: [L, M],
                    dots: C,
                    items: B,
                    responsive: {
                        0: {
                            items: H
                        },
                        480: {
                            items: G
                        },
                        768: {
                            items: F
                        },
                        992: {
                            items: E
                        },
                        1200: {
                            items: B
                        }
                    }
                })
            })
        }
    }

    function d() {
        a(".ps-select").selectpicker()
    }

    function r() {
        var z = a(".ps-search-btn"),
            y = a(".ps-search__close"),
            x = a(".ps-search");
        z.on("click", function(A) {
            A.preventDefault();
            x.addClass("open")
        });
        y.on("click", function(A) {
            A.preventDefault();
            x.removeClass("open")
        })
    }

    function p() {
        a("select.ps-rating").barrating({
            theme: "fontawesome-stars"
        })
    }

    function e() {
        var x = a(".number");
        x.each(function() {
            var y = a(this);
            y.text("0");
            var z = new Waypoint({
                element: y,
                handler: function(A) {
                    y.countTo({
                        speed: "1500",
                        refreshInterval: 50
                    });
                    this.destroy()
                },
                offset: function() {
                    return Waypoint.viewportHeight() - y.outerHeight() - 100
                }
            })
        })
    }

    function j() {
        a.gmap3({
            key: "AIzaSyAx39JFH5nhxze1ZydH-Kl8xXM3OK4fvcg"
        });
        var x = a("#contact-map");
        if (x.length > 0) {
            x.gmap3({
                address: x.data("address"),
                zoom: x.data("zoom"),
                scrollwheel: false,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                scrollwheel: false
            }).marker(function(y) {
                return {
                    position: y.getCenter(),
                    icon: "images/marker.png",
                    animation: google.maps.Animation.BOUNCE
                }
            }).infowindow({
                content: x.data("address")
            }).then(function(y) {
                var z = this.get(0);
                var A = this.get(1);
                A.addListener("click", function() {
                    y.open(z, A)
                })
            })
        } else {
            console.log("Notice: Don't have map on this page!!!")
        }
    }

    function t() {
        var x = a(".ps-status-bar");
        x.each(function(y) {
            var z = a(this).data("width");
            a(this).children("span").css("width", z + "%")
        })
    }

    function f() {
        var z = a(".ps-slider");
        var C = z.siblings().find(".ps-slider__min");
        var A = z.siblings().find(".ps-slider__max");
        var y = z.data("default-min");
        var x = z.data("default-max");
        var B = z.data("max");
        var D = z.data("step");
        if (z.length > 0) {
            z.slider({
                min: 0,
                max: B,
                step: D,
                range: true,
                values: [y, x],
                slide: function(F, G) {
                    var H = G.values;
                    C.text("$" + H[0]);
                    A.text("$" + H[1])
                }
            });
            var E = z.slider("option", "values");
            console.log(E[1]);
            C.text("$" + E[0]);
            A.text("$" + E[1])
        } else {}
    }

    function s() {
        if (a(".ps-product--detail").length > 0) {
            var x = a(".ps-product__image"),
                y = a(".ps-product__variants"),
                z = false;
            x.slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                asNavFor: ".ps-product__variants",
                dots: false,
                arrows: false,
            });
            y.slick({
                slidesToShow: 4,
                slidesToScroll: 1,
                arrows: false,
                arrow: false,
                focusOnSelect: true,
                asNavFor: ".ps-product__image",
                vertical: z,
                responsive: [{
                    breakpoint: 992,
                    settings: {
                        arrows: false,
                        slidesToShow: 4,
                        vertical: false
                    }
                }, {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 3,
                        vertical: false
                    }
                }, ]
            })
        }
    }

    function o() {
        a(".ps-product__image").lightGallery({
            selector: ".item a",
            thumbnail: true
        });
        a(".ps-video").lightGallery()
    }

    function g() {
        var x = a(".ps-number");
        x.each(function() {
            var y = a(this),
                z = y.find("input").val();
            y.find(".up").on("click", function(A) {
                A.preventDefault();
                z++;
                y.find("input").val(z);
                y.find("input").attr("value", z)
            });
            y.find(".down").on("click", function(A) {
                A.preventDefault();
                if (z > 1) {
                    z--;
                    y.find("input").val(z);
                    y.find("input").attr("value", z)
                }
            })
        });
        a(".form-group--number").each(function() {
            var y = a(this),
                z = y.find("input").val();
            y.find(".plus").on("click", function(A) {
                A.preventDefault();
                z++;
                y.find("input").val(z);
                y.find("input").attr("value", z)
            });
            y.find(".minus").on("click", function(A) {
                A.preventDefault();
                if (z > 1) {
                    z--;
                    y.find("input").val(z);
                    y.find("input").attr("value", z)
                }
            })
        })
    }

    function i() {
        var x = a("#slider .ps-carousel--animate");
        x.slick({
            autoplay: true,
            speed: 1000,
            lazyLoad: "progressive",
            arrows: true,
            fade: true,
            prevArrow: "<i class='slider-prev ba-back'></i>",
            nextArrow: "<i class='slider-next ba-next'></i>"
        }).slickAnimation()
    }

    function c() {
        var y = 0;
        var x = a("#back2top");
        a(window).scroll(function() {
            var z = a(window).scrollTop();
            if (z > y) {
                if (z > 500) {
                    x.addClass("active")
                } else {
                    x.removeClass("active")
                }
            } else {
                x.removeClass("active")
            }
            y = z
        });
        x.on("click", function() {
            a("html, body").animate({
                scrollTop: "0px"
            }, 800)
        })
    }

    function w() {
        var x = a("#subscribe"),
            y = x.data("time");
        setTimeout(function() {
            if (x.length > 0) {
                x.addClass("active");
                a("body").css("overflow", "hidden")
            }
        }, y);
        a(".ps-popup__close").on("click", function(z) {
            z.preventDefault();
            a(this).closest(".ps-popup").removeClass("active");
            a("body").css("overflow", "auto")
        })
    }

    function l() {
        var x = a(".ps-cart");
        if (x.length > 0) {
            x.find(".ps-cart__content").slimScroll({
                color: "#ffcc00",
            })
        }
    }
    a(function() {
        b();
        n();
        m();
        d();
        k();
        v();
        r();
        p();
        e();
        j();
        t();
        f();
        s();
        o();
        g();
        i();
        c();
        l()
    });
    a(window).on("load", function() {
        a(".ps-loading").addClass("loaded");
        w()
    });
    a(window).on("load resize", function() {
        q();
        u()
    })
})(jQuery);