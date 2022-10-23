(*
    Maria Moșneag
    343C1
*)

class Commands inherits IO {
    inp : Input <- new Input;
    
    help(str : String, lists : List) : List {
        {
            if not (str = "help")
            then abort()
            else
                let outstr : String <- new String
                in
                {
                    outstr.concat("\nOptions:\n");
                    outstr.concat("help\n");
                    outstr.concat("load\n");
                    outstr.concat("print\n");
                    outstr.concat("merge <index1> <index2>\n");

                    outstr.concat("filterBy <index> ");
                    outstr.concat("[ProductFilter/RankFilter/");
                    outstr.concat("SamePriceFilter]\n");

                    outstr.concat("sortBy <index> ");
                    outstr.concat("[PriceComparator/RankComparator/");
                    outstr.concat("AlphabeticComparator] ");
                    outstr.concat("[ascendent/descendent]\n");

                    outstr.concat("\n");

                    out_string(outstr);
                }
            fi;

            lists;
        }
    };

    load(str : String, lists : List) : List {
        {
            if not str = "load"
            then abort()
            else true
            fi;

            let list : List <- new List,
                loading : Bool <- true,
                somestr : String
            in
            {
                while loading loop
                    {
                        somestr <- in_string();
                        if somestr = "END"
                        then loading <- false
                        else list.add(inp.parse_obj(somestr))
                        fi;
                    }
                pool;

                lists <- lists.add(list);
            };

            lists;
        }
    };

    print(str : String, lists : List) : List {
        {
            if str = "print"
            then
                let crt_index : Int <- 1,
                    crt_lists : List <- lists,
                    converter : A2I <- new A2I
                in
                {
                    while not crt_lists.isEmpty() loop
                    {
                        out_string(converter.i2a(crt_index));
                        out_string(": ");
                        case crt_lists.head() of
                        l: List => out_string(l.toString());
                        esac;
                        out_string("\n");

                        crt_lists <- crt_lists.tail();
                        crt_index <- crt_index + 1;
                    }
                    pool;
                }
            else
            {
                let start : Int <- "print".length(),
                    rest : String <- inp.rest_substr(start, str),
                    str_index : String <- inp.next_token(rest),
                    extra : String <- inp.del_leading_spaces(inp.skip_token(rest)),
                    converter : A2I <- new A2I,
                    index : Int <- converter.a2i(str_index) - 1
                in
                    if not extra = ""
                    then abort()
                    else
                        case lists.get(index) of
                        l: List => out_string(l.toString().concat("\n"));
                        esac
                    fi;
            }
            fi;

            lists;
        }
    };

    merge(str : String, lists : List) : List {
        {
            let rest1 : String <- inp.skip_token(str),
                str_index1 : String <- inp.next_token(rest1),
                rest2 : String <- inp.skip_token(rest1),
                str_index2 : String <- inp.next_token(rest2),
                extra : String <- inp.del_leading_spaces(inp.skip_token(rest2)),
                converter : A2I <- new A2I,
                index1 : Int <- converter.a2i(str_index1) - 1,
                index2 : Int <- converter.a2i(str_index2) - 1,
                list1 : Object <- lists.get(index1),
                list2 : Object <- lists.get(index2)
            in
            {
                if not extra = ""
                then abort()
                else true
                fi;

                if str_index2 = ""
                then abort()
                else true
                fi;

                if str_index1 = ""
                then abort()
                else true
                fi;

                case list1 of
                l1: List =>
                    case list2 of
                    l2: List =>
                    {
                        lists <- lists.del(index1);

                        if index1 < index2
                        then lists <- lists.del(index2 - 1)
                        else lists <- lists.del(index2)
                        fi;

                        lists <- lists.add(l1.merge(l2));
                        
                    };
                    esac;
                esac;
            };

            lists;
        }
    };

    filterBy(str : String, lists : List) : List {
        {
            let rest1 : String <- inp.skip_token(str),
                str_index : String <- inp.next_token(rest1),
                rest2 : String <- inp.skip_token(rest1),
                type : String <- inp.next_token(rest2),
                extra : String <- inp.del_leading_spaces(inp.skip_token(rest2)),
                filter : Filter,
                converter : A2I <- new A2I,
                index : Int <- converter.a2i(str_index) - 1
            in
            {
                if not extra = ""
                then abort()
                else
                    if type = ""
                    then abort()
                    else
                        if str_index = ""
                        then abort()
                        else
                            if type = "ProductFilter"
                            then filter <- new ProductFilter
                            else
                                if type = "RankFilter"
                                then filter <- new RankFilter
                                else
                                    if type = "SamePriceFilter"
                                    then filter <- new SamePriceFilter
                                    else abort()                                        
                                    fi
                                fi
                            fi
                        fi
                    fi
                fi;

                case lists.get(index) of
                l: List =>
                {
                    l <- l.filterBy(filter);
                    lists <- lists.replace(index, l);
                };
                esac;
            };

            lists;
        }
    };

    sortBy(str : String, lists : List) : List {
        {
            let rest1 : String <- inp.skip_token(str),
                str_index : String <- inp.next_token(rest1),
                rest2 : String <- inp.skip_token(rest1),
                type_str : String <- inp.next_token(rest2),
                rest3 : String <- inp.skip_token(rest2),
                ord_str : String <- inp.next_token(rest3),
                extra : String <- inp.del_leading_spaces(inp.skip_token(rest3)),
                comp : Comparator,
                converter : A2I <- new A2I,
                index : Int <- converter.a2i(str_index) - 1,
                ord : Int
            in
            {
                if not extra = ""
                then abort()
                else true
                fi;

                if ord_str = ""
                then abort()
                else
                    if ord_str = "ascendent"
                    then ord <- 1
                    else
                        if ord_str = "descendent"
                        then ord <- 0 - 1
                        else abort()
                        fi
                    fi
                fi;

                if type_str = ""
                then abort()
                else
                    if type_str = "PriceComparator"
                    then comp <- new PriceComparator
                    else
                        if type_str = "RankComparator"
                        then comp <- new RankComparator
                        else
                            if type_str = "AlphabeticComparator"
                            then comp <- new AlphabeticComparator
                            else abort()                                
                            fi
                        fi
                    fi
                fi;

                if str_index = ""
                then abort()
                else true
                fi;

                case lists.get(index) of
                l: List =>
                {
                    l <- l.sortBy(comp, ord);
                    lists <- lists.replace(index, l);
                };
                esac;
            };

            lists;
        }
    };

    parse(str : String, lists : List) : List {
        {
            if str.length() < "help".length() then abort() else
                -- help
                if str.substr(0, "help".length()) = "help"
                then help(str, lists)
                else
                    -- load
                    if str.substr(0, "load".length()) = "load"
                    then lists <- load(str, lists)
                    else
                        if str.length() < "print".length()
                        then abort()
                        else
                            -- print
                            if str.substr(0, "print".length()) = "print"
                            then print(str, lists)
                            else
                                -- merge
                                if str.substr(0, "merge".length()) = "merge"
                                then lists <- merge(str, lists)
                                else
                                    if str.length() < "sortBy".length()
                                    then abort()
                                    else
                                        -- sortBy
                                        if str.substr(0, "sortBy".length()) = "sortBy"
                                        then lists <- sortBy(str, lists)
                                        else
                                            if str.length() < "filterBy".length()
                                            then abort()
                                            else
                                                -- filterBy
                                                if str.substr(0, "filterBy".length()) = "filterBy"
                                                then lists <- filterBy(str, lists)
                                                else
                                                    abort()
                                                fi
                                            fi
                                        fi
                                    fi
                                fi
                            fi
                        fi
                    fi
                fi
            fi;

            lists;
        }
    };

};
