(*
    Maria Moșneag
    343C1
*)

class Input inherits IO {
    parse_obj(str : String) : Object {
        let cls : String <- next_token(str),
            rest1 : String <- skip_token(str),
            atr1 : String <- next_token(rest1),
            rest2 : String <- skip_token(rest1),
            atr2 : String <- next_token(rest2),
            rest3 : String <- skip_token(rest2),
            atr3 : String <- next_token(rest3),
            extra : String <- del_leading_spaces(skip_token(rest3)),
            res : Object,
            converter : A2I <- new A2I,
            no_atr : Int <- 0
        in
        {
            if not extra = ""
            then abort()
            else true
            fi;

            -- classes with no attributes
            if cls = "IO"
            then {res <- new IO; no_atr <- 0;}
            else true
            fi;

            -- classes with one attribute
            if isvoid res
            then
                if atr1 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if cls = "Int"
            then {res <- converter.a2i(atr1); no_atr <- 1;}
            else true
            fi;

            if cls = "String"
            then {res <- atr1; no_atr <- 1;}
            else true
            fi;

            if cls = "Bool"
            then
                if atr1 = "true"
                then {res <- true; no_atr <- 1;}
                else
                    if atr1 = "false"
                    then {res <- false; no_atr <- 1;}
                    else true
                    fi
                fi
            else true
            fi;

            if cls = "Rank"
            then {res <- new Rank.init(atr1); no_atr <- 1;}
            else true
            fi;

            if cls = "Private"
            then {res <- new Private.init(atr1); no_atr <- 1;}
            else true
            fi;

            if cls = "Corporal"
            then {res <- new Corporal.init(atr1); no_atr <- 1;}
            else true
            fi;

            if cls = "Sergent"
            then {res <- new Sergent.init(atr1); no_atr <- 1;}
            else true
            fi;

            if cls = "Officer"
            then {res <- new Officer.init(atr1); no_atr <- 1;}
            else true
            fi;

            -- classes with three attributes
            if isvoid res
            then
                if atr2 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if isvoid res
            then
                if atr3 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if cls = "Product"
            then
            {
                res <- new Product.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if cls = "Edible"
            then
            {
                res <- new Edible.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if cls = "Soda"
            then
            {
                res <- new Soda.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if cls = "Coffee"
            then
            {
                res <- new Coffee.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if cls = "Laptop"
            then
            {
                res <- new Laptop.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if cls = "Router"
            then
            {
                res <- new Router.init(atr1, atr2, converter.a2i(atr3));
                no_atr <- 3;
            }
            else true
            fi;

            if no_atr < 1
            then
                if not atr1 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if no_atr < 2
            then
                if not atr2 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if no_atr < 3
            then
                if not atr3 = ""
                then abort()
                else true
                fi
            else true
            fi;

            if isvoid res
            then abort()
            else res
            fi;
        }
    };

    skip_token(str : String) : String {
        let s : String <- del_leading_spaces(str),
            token_len : Int <- s.length(),
            pos : Int <- 0,
            len : Int <- s.length()
        in
            {
                while pos < len loop
                    if s.substr(pos, 1) = " "
                    then
                    {
                        token_len <- pos;
                        pos <- len;
                    }
                    else pos <- pos + 1
                    fi
                pool;
                s.substr(token_len, len - token_len);
            }
    };

    next_token(str : String) : String {
        let s : String <- del_leading_spaces(str),
            token_len : Int <- s.length(),
            pos : Int <- 0,
            len : Int <- s.length()
        in
            {
                while pos < len loop
                    if s.substr(pos, 1) = " "
                    then
                    {
                        token_len <- pos;
                        pos <- len;
                    }
                    else pos <- pos + 1
                    fi
                pool;
                s.substr(0, token_len);
            }
    };

    del_leading_spaces(str : String) : String {
        if str.length() = 0
        then str
        else
            let start : Int <- str.length() - 1,
                pos : Int <- 0,
                len : Int <- str.length()
            in
                {
                    while pos < len loop
                        if str.substr(pos, 1) = " "
                        then pos <- pos + 1
                        else
                        {
                            start <- pos;
                            pos <- len;
                        }
                        fi
                    pool;

                    if str.substr(start, len - start) = " "
                    then ""
                    else str.substr(start, len - start)
                    fi;
                }
        fi
    };

    rest_substr(index : Int, str : String) : String {
        let len : Int <- str.length()
        in
            str.substr(index, len - index)
    };
};
