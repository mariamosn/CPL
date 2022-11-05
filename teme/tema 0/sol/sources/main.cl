(*
    Maria Moșneag
    343C1
*)

class Main inherits IO{
    lists : List <- new List;
    looping : Bool <- true;
    somestr : String;
    parser : Commands <- new Commands;

    main() : Object {
        {
            lists <- parser.parse("load", lists);

            while looping loop
            {
                somestr <- in_string();
                lists <- parser.parse(somestr, lists);
            }
            pool;
        }
    };
};
