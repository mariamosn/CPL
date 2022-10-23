(*
    Maria Moșneag
    343C1
*)

class List {

    hd : Object;
    tail : List;

    isEmpty() : Bool {
        isvoid hd
    };

    head() : Object {
        hd
    };

    tail() : List {
        tail
    };

    build(h : Object, t : List) : SELF_TYPE {
        {
            hd <- h;
            tail <- t;
            self;
        }
    };

    add(o : Object) : SELF_TYPE {
        {
            if self.isEmpty()
            then
                {
                    hd <- o;
                    tail <- new List;
                }
            else tail.add(o)
            fi;
            self;
        }
    };

    toString() : String {
        let crt_list : List <- self,
            str : String <- "[ ",
            converter : A2I <- new A2I
        in
        {
            while not crt_list.isEmpty() loop
            {
                case crt_list.head() of
                s: String => str <- str.concat("String(")
                                        .concat(s)
                                        .concat(")");
                i: Int => str <- str.concat("Int(")
                                    .concat(converter.i2a(i))
                                    .concat(")");
                b: Bool => str <- str.concat("Bool(")
                                    .concat(converter.b2a(b))
                                    .concat(")");
                io: IO => str <- str.concat("IO()");

                edible: Edible => str <- str.concat(edible.toString());
                soda: Soda => str <- str.concat(soda.toString());
                coffee: Coffee => str <- str.concat(coffee.toString());
                laptop: Laptop => str <- str.concat(laptop.toString());
                router: Router => str <- str.concat(router.toString());

                product: Product => str <- str.concat(product.toString());
                private: Private => str <- str.concat(private.toString());
                corporal: Corporal => str <- str.concat(corporal.toString());
                sergent: Sergent => str <- str.concat(sergent.toString());
                officer: Officer => str <- str.concat(officer.toString());
                rank: Rank => str <- str.concat(rank.toString());
                esac;

                if not crt_list.tail().isEmpty()
                then str <- str.concat(", ")
                else str
                fi;
                crt_list <- crt_list.tail();
            }
            pool;
            str.concat(" ]");
        }
    };

    merge(other : List) : SELF_TYPE {
        {
            while not other.isEmpty() loop
            {
                self.add(other.head());
                other <- other.tail();
            }
            pool;
            self;
        }
    };

    filterBy(f : Filter) : List {
        if f.filter(hd)
        then
            if tail.isEmpty()
            then self
            else new List.build(hd, tail.filterBy(f))
            fi
        else
            if not tail.isEmpty()
            then tail.filterBy(f)
            else tail
            fi
        fi
    };

    sortBy(c : Comparator, ord : Int) : List {
        aux_sort(new List, c, ord, self)
    };

    aux_sort(sorted : List, c : Comparator, ord : Int, rest : List) : List {
        if rest.isEmpty()
        then sorted
        else
            aux_sort(insert_sorted(sorted, rest.head(), c, ord),
                    c, ord, rest.tail())
        fi
    };

    insert_sorted(sorted : List, elem : Object, c : Comparator, ord : Int) : List {
        if isvoid elem
        then sorted
        else
            if sorted.isEmpty()
            then new List.build(elem, sorted)
            else
                if c.compareTo(elem, sorted.head(), ord) <= 0
                then new List.build(elem, sorted)
                else new List.build(sorted.head(),
                                    insert_sorted(sorted.tail(), elem, c, ord))
                fi
            fi
        fi
    };

    get(index : Int) : Object {
        if index < 0
        then abort()
        else aux_get(index, 0, self)
        fi
    };

    aux_get(index : Int, crt : Int, list : List) : Object {
        if list.isEmpty()
        then abort()
        else
            if index = crt
            then list.head()
            else
                aux_get(index, crt + 1, list.tail())
            fi
        fi
    };

    del(index : Int) : List {
        if index < 0
        then self
        else aux_del(index, 0, self)
        fi
    };

    aux_del(index : Int, crt : Int, list : List) : List {
        if list.isEmpty()
        then list
        else
            if index = crt
            then list.tail()
            else
                new List.build(list.head(), aux_del(index, crt + 1, list.tail()))
            fi
        fi
    };

    replace(index : Int, o : Object) : List {
        if index < 0
        then self
        else aux_replace(index, 0, self, o)
        fi
    };

    aux_replace(index : Int, crt : Int, list : List, o : Object) : List {
        if list.isEmpty()
        then list
        else
            if index = crt
            then new List.build(o, list.tail())
            else
                new List.build(list.head(),
                                aux_replace(index, crt + 1, list.tail(), o))
            fi
        fi
    };
};
