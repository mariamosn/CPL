(*
    Maria Moșneag
    343C1
*)

(*
    ord = 1 => asc
    ord = -1 => desc
*)
class Comparator {
    compareTo(o1 : Object, o2 : Object, ord : Int):Int {0};
};

class Filter {
    filter(o : Object):Bool {true};
};

(* specified comparators and filters*)

class ProductFilter inherits Filter {
    filter(o : Object) : Bool {
        if isvoid o
        then false
        else
            case o of
            a: Soda => true;
            b: Coffee => true;
            c: Edible => true;
            d: Laptop => true;
            e: Router => true;
            f: Object => false;
            esac
        fi
    };
};

class RankFilter inherits Filter {
    filter(o : Object) : Bool {
        if isvoid o
        then false
        else
            case o of
            a: Officer => true;
            b: Sergent => true;
            c: Corporal => true;
            d: Private => true;
            f: Object => false;
            esac
        fi
    };
};

class SamePriceFilter inherits Filter {
    filter(o : Object) : Bool {
        if isvoid o
        then false
        else
            case o of
            a: Soda => a.getprice() = a@Product.getprice();
            b: Coffee => b.getprice() = b@Product.getprice();
            c: Edible => c.getprice() = c@Product.getprice();
            d: Laptop => d.getprice() = d@Product.getprice();
            e: Router => e.getprice() = e@Product.getprice();
            f: Object => false;
            esac
        fi
    };
};

class PriceComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object, ord : Int) : Int {
        if isvoid o2
        then 1
        else
            case o1 of
            p1: Product =>
                case o2 of
                p2: Product => (p1.getprice() - p2.getprice()) * ord;
                esac;
            esac
        fi
    };
};

class RankComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object, ord : Int) : Int {
        if isvoid o2
        then 1
        else
            case o1 of
            r1: Rank =>
                case o2 of
                r2: Rank => (getord(r1) - getord(r2)) * ord;
                esac;
            esac
        fi
    };

    getord(o : Rank) : Int {
        case o of
        a: Officer => 4;
        b: Sergent => 3;
        c: Corporal => 2;
        d: Private => 1;
        esac
    };
};

class AlphabeticComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object, ord : Int) : Int {
        if isvoid o2
        then 1
        else
            case o1 of
            s1: String =>
                case o2 of
                s2: String =>
                    if s1 < s2
                    then (0 - 1) * ord
                    else
                        if s1 = s2
                        then 0
                        else 1 * ord
                        fi
                    fi;
                esac;
            esac
        fi
    };
};
