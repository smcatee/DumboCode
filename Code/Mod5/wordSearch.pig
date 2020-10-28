textLines = LOAD '/user/sm9553/RBDA/Mod5/wordCountInput.txt' AS (tweet:chararray);
searchTerms = LOAD '/user/sm9553/RBDA/Mod5/searchTerms.txt' AS (term:chararray);


textLines_grp = GROUP textLines ALL;

textLines_each = FOREACH textLines_grp 
                   {
                      Chicago = FILTER textLines BY tweet matches '.*Chicago.*';
                      Dec = FILTER textLines BY tweet matches '.*Dec.*' ;
                      Java = FILTER textLines BY tweet matches '.*Java.*' ;
                      hackathon = FILTER textLines BY tweet matches '.*hackathon.*' ;

                    GENERATE COUNT(Chicago) as Chicago_cnt, COUNT(Dec) as Dec_cnt, COUNT(Java) as Java_cnt, COUNT(hackathon) as hackathon_cnt;
                   };
dump textLines_each;

counts = FOREACH textLines_each GENERATE FLATTEN(TOBAG(*));

counts_ranked = rank counts;
searchTerms_ranked = rank searchTerms;


fullJoin = JOIN searchTerms_ranked BY rank_searchTerms FULL OUTER, counts_ranked BY rank_counts;
final = FOREACH fullJoin GENERATE $1, $3;

dump final;
/* Chicago, Dec, Java, hackathon */
