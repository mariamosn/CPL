(*
    Laborator COOL.
*)

(*
    Exercițiul 1.

    Implementați funcția fibonacci, utilizând atât varianta recursivă,
    cât și cea iterativă.
*)
class Fibo {
    fibo_rec(n : Int) : Int {
        if n <= 1 then n else fibo_rec(n - 1) + fibo_rec(n - 2) fi
    };

    fibo_iter(n : Int) : Int {
        let a : Int <- 0,
            b : Int <- 1,
            c : Int,
            cnt : Int <- 0
        in
            {
                while (cnt < n) loop
                    {
                        cnt <- cnt + 1;
                        c <- a + b;
                        a <- b;
                        b <- c;
                    }
                pool;
                a;
            }
    };
};
    
(*
    Exercițiul 2.

    Pornind de la ierarhia de clase implementată la curs, aferentă listelor
    (găsiți clasele List și Cons mai jos), implementați următoarele funcții
    și testați-le. Este necesară definirea lor în clasa List și supradefinirea
    în clasa Cons.

    * append: întoarce o nouă listă rezultată prin concatenarea listei curente
        (self) cu lista dată ca parametru;
    * reverse: întoarce o nouă listă cu elementele în ordine inversă.
*)

(*
    Listă omogenă cu elemente de tip Int. Clasa List constituie rădăcina
    ierarhiei de clase reprezentând liste, codificând în același timp
    o listă vidă.

    Adaptare după arhiva oficială de exemple a limbajului COOL.
*)
class List inherits IO {
    isEmpty() : Bool { true };

    -- 0, deși cod mort, este necesar pentru verificarea tipurilor
    hd() : Int { { abort(); 0; } };

    -- Similar pentru self
    tl() : List { { abort(); self; } };

    cons(h : Int) : Cons {
        new Cons.init(h, self)
    };

    print() : IO { out_string("\n") };

    append(l : List) : List {
        l
    };

    reverse() : List { self };

    map(m : Map) : List { self };

    filter(f : Filter) : List { self };
};

(*
    În privința vizibilității, atributele sunt implicit protejate, iar metodele,
    publice.

    Atributele și metodele utilizează spații de nume diferite, motiv pentru care
    hd și tl reprezintă nume atât de atribute, cât și de metode.
*)
class Cons inherits List {
    hd : Int;
    tl : List;

    init(h : Int, t : List) : Cons {
        {
            hd <- h;
            tl <- t;
            self;
        }
    };

    -- Supradefinirea funcțiilor din clasa List
    isEmpty() : Bool { false };

    hd() : Int { hd };

    tl() : List { tl };

    print() : IO {
        {
            out_int(hd);
            out_string(" ");
            -- Mecanismul de dynamic dispatch asigură alegerea implementării
            -- corecte a metodei print.
            tl.print();
        }
    };

    append(l : List) : List {
        tl.append(l).cons(hd)
    };

    reverse() : List {
        tl.reverse().append(new Cons.init(hd, new List))
    };

    map(m : Map) : List {
        tl.map(m).cons(m.apply(hd))
    };

    filter(f : Filter) : List {
        if f.apply(hd) then tl.filter(f).cons(hd) else tl.filter(f) fi
    };
};

(*
    Exercițiul 3.

    Scopul este implementarea unor mecanisme similare funcționalelor
    map și filter din limbajele funcționale. map aplică o funcție pe fiecare
    element, iar filter reține doar elementele care satisfac o anumită condiție.
    Ambele întorc o nouă listă.

    Definiți clasele schelet Map, respectiv Filter, care vor include unica
    metodă apply, având tipul potrivit în fiecare clasă, și implementare
    de formă.

    Pentru a defini o funcție utilă, care adună 1 la fiecare element al listei,
    definiți o subclasă a lui Map, cu implementarea corectă a metodei apply.

    În final, definiți în cadrul ierarhiei List-Cons o metodă map, care primește
    un parametru de tipul Map.

    Definiți o subclasă a subclasei de mai sus, care, pe lângă funcționalitatea
    existentă, de incrementare cu 1 a fiecărui element, contorizează intern
    și numărul de elemente prelucrate. Utilizați static dispatch pentru apelarea
    metodei de incrementare, deja definită.

    Repetați pentru clasa Filter, cu o implementare la alegere a metodei apply.
*)

class Map {
    apply(x : Int) : Int { 0 };
};

class IncMap inherits Map {
    apply(x : Int) : Int { x + 1 };
};

class CntIncMap inherits IncMap {
    cnt : Int;

    apply(x : Int) : Int  {
        {
            cnt <- cnt + 1;
            self@IncMap.apply(x);
        }
    };

    getCnt() : Int { cnt };
};

class Filter {
    apply(x : Int) : Bool { true };
};

class MyFilter inherits Filter {
    apply(x : Int) : Bool {
        if x < 10 then true else false fi
    };
};

-- Testați în main.
class Main inherits IO {
    main() : Object {
        let list : List <- new List.cons(1).cons(2).cons(3),
            temp : List <- list,
            -- Pentru Ex. 1
            fib : Fibo <- new Fibo,
            -- Pentru Ex. 2
            l1 : List <- new List.cons(4).cons(5).cons(6),
            l2 : List <- new List.cons(7).cons(8).cons(9),
            l3 : List <- new List,
            -- Pentru Ex. 3
            incmap : IncMap <- new IncMap,
            cntmap : CntIncMap <- new CntIncMap
        in
            {
                -- Afișare utilizând o buclă while. Mecanismul de dynamic
                -- dispatch asigură alegerea implementării corecte a metodei
                -- isEmpty, din clasele List, respectiv Cons.
                while (not temp.isEmpty()) loop
                    {
                        out_int(temp.hd());
                        out_string(" ");
                        temp <- temp.tl();
                    }
                pool;

                out_string("\n");

                -- Afișare utilizând metoda din clasele pe liste.
                list.print();

                -- Ex. 1
                out_int(fib.fibo_rec(10));
                out_string("\n");
                out_int(fib.fibo_iter(10));
                out_string("\n");

                -- Ex. 2
                out_string("\nl1: ");
                l1.print();
                out_string("l2: ");
                l2.print();
                out_string("l3: ");
                l3.print();

                -- append
                out_string("l1 + l2: ");
                l1.append(l2).print();
                out_string("l1 + l3: ");
                l1.append(l3).print();
                out_string("l2 + l3: ");
                l2.append(l3).print();
                out_string("l2 + l1: ");
                l2.append(l1).print();
                out_string("l3 + l1: ");
                l3.append(l1).print();
                out_string("l3 + l2: ");
                l3.append(l2).print();

                -- reverse
                out_string("rev l1: ");
                l1.reverse().print();
                out_string("rev l2: ");
                l2.reverse().print();
                out_string("rev l3: ");
                l3.reverse().print();

                -- Ex. 3
                out_string("l1 map inc: ");
                l1.map(incmap).print();
                out_string("l2 map inc + cnt: ");
                l2.map(cntmap).print();
                out_string("cnt: ");
                out_int(cntmap.getCnt());
                out_string("\n");
            }
    };
};