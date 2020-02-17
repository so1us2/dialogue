# Report
<!-- Run SimulationTest to regenerate this report. -->
```
                                all_nodes_500[CONCURRENCY_LIMITER].txt:	success=50.0%	client_mean=PT0.6S         	server_cpu=PT2M           	received=200/200	codes={200=100, 500=100}
                                        all_nodes_500[ROUND_ROBIN].txt:	success=50.0%	client_mean=PT0.6S         	server_cpu=PT2M           	received=200/200	codes={200=100, 500=100}
                                   black_hole[CONCURRENCY_LIMITER].txt:	success=90.0%	client_mean=PT0.6S         	server_cpu=PT1M48S        	received=180/200	codes={200=180}
                                           black_hole[ROUND_ROBIN].txt:	success=65.0%	client_mean=PT0.6S         	server_cpu=PT1M18S        	received=130/200	codes={200=130}
                             drastic_slowdown[CONCURRENCY_LIMITER].txt:	success=100.0%	client_mean=PT0.131675583S 	server_cpu=PT8M46.702333319S	received=4000/4000	codes={200=4000}
                                     drastic_slowdown[ROUND_ROBIN].txt:	success=100.0%	client_mean=PT8.340639499S 	server_cpu=PT9H16M2.557999978S	received=4000/4000	codes={200=4000}
                        fast_500s_then_revert[CONCURRENCY_LIMITER].txt:	success=76.7%	client_mean=PT0.055281733S 	server_cpu=PT3M27.306499709S	received=3750/3750	codes={200=2875, 500=875}
                                fast_500s_then_revert[ROUND_ROBIN].txt:	success=76.7%	client_mean=PT0.055281733S 	server_cpu=PT3M27.306499709S	received=3750/3750	codes={200=2875, 500=875}
                               live_reloading[CONCURRENCY_LIMITER].txt:	success=58.3%	client_mean=PT0.7228S      	server_cpu=PT4M49.12S     	received=400/400	codes={200=233, 500=167}
                                       live_reloading[ROUND_ROBIN].txt:	success=58.3%	client_mean=PT0.7228S      	server_cpu=PT4M49.12S     	received=400/400	codes={200=233, 500=167}
             one_endpoint_dies_on_each_server[CONCURRENCY_LIMITER].txt:	success=67.6%	client_mean=PT0.6S         	server_cpu=PT5M6S         	received=510/510	codes={200=345, 500=165}
                     one_endpoint_dies_on_each_server[ROUND_ROBIN].txt:	success=67.6%	client_mean=PT0.6S         	server_cpu=PT5M6S         	received=510/510	codes={200=345, 500=165}
                       simplest_possible_case[CONCURRENCY_LIMITER].txt:	success=100.0%	client_mean=PT0.7998S      	server_cpu=PT13M19.8S     	received=1000/1000	codes={200=1000}
                               simplest_possible_case[ROUND_ROBIN].txt:	success=100.0%	client_mean=PT0.7998S      	server_cpu=PT13M19.8S     	received=1000/1000	codes={200=1000}
                        slow_503s_then_revert[CONCURRENCY_LIMITER].txt:	success=100.0%	client_mean=PT0.129949111S 	server_cpu=PT6M29.847333275S	received=3000/3000	codes={200=3000}
                                slow_503s_then_revert[ROUND_ROBIN].txt:	success=100.0%	client_mean=PT1.408771222S 	server_cpu=PT1H10M26.313666644S	received=3000/3000	codes={200=3000}
                slowdown_and_error_thresholds[CONCURRENCY_LIMITER].txt:	success=100.0%	client_mean=PT1.977419999S 	server_cpu=PT31M8.473333135S	received=1000/1000	codes={200=1000}
                        slowdown_and_error_thresholds[ROUND_ROBIN].txt:	success=77.1%	client_mean=PT2.231446666S 	server_cpu=PT37M11.446666464S	received=1000/1000	codes={200=771, 500=229}
```



## all_nodes_500[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/all_nodes_500[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="all_nodes_500[CONCURRENCY_LIMITER].png" /></td></tr></table>


## all_nodes_500[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/all_nodes_500[ROUND_ROBIN].png" /></td><td><image width=400 src="all_nodes_500[ROUND_ROBIN].png" /></td></tr></table>


## black_hole[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/black_hole[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="black_hole[CONCURRENCY_LIMITER].png" /></td></tr></table>


## black_hole[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/black_hole[ROUND_ROBIN].png" /></td><td><image width=400 src="black_hole[ROUND_ROBIN].png" /></td></tr></table>


## drastic_slowdown[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/drastic_slowdown[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="drastic_slowdown[CONCURRENCY_LIMITER].png" /></td></tr></table>


## drastic_slowdown[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/drastic_slowdown[ROUND_ROBIN].png" /></td><td><image width=400 src="drastic_slowdown[ROUND_ROBIN].png" /></td></tr></table>


## fast_500s_then_revert[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/fast_500s_then_revert[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="fast_500s_then_revert[CONCURRENCY_LIMITER].png" /></td></tr></table>


## fast_500s_then_revert[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/fast_500s_then_revert[ROUND_ROBIN].png" /></td><td><image width=400 src="fast_500s_then_revert[ROUND_ROBIN].png" /></td></tr></table>


## live_reloading[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/live_reloading[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="live_reloading[CONCURRENCY_LIMITER].png" /></td></tr></table>


## live_reloading[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/live_reloading[ROUND_ROBIN].png" /></td><td><image width=400 src="live_reloading[ROUND_ROBIN].png" /></td></tr></table>


## one_endpoint_dies_on_each_server[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/one_endpoint_dies_on_each_server[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="one_endpoint_dies_on_each_server[CONCURRENCY_LIMITER].png" /></td></tr></table>


## one_endpoint_dies_on_each_server[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/one_endpoint_dies_on_each_server[ROUND_ROBIN].png" /></td><td><image width=400 src="one_endpoint_dies_on_each_server[ROUND_ROBIN].png" /></td></tr></table>


## simplest_possible_case[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/simplest_possible_case[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="simplest_possible_case[CONCURRENCY_LIMITER].png" /></td></tr></table>


## simplest_possible_case[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/simplest_possible_case[ROUND_ROBIN].png" /></td><td><image width=400 src="simplest_possible_case[ROUND_ROBIN].png" /></td></tr></table>


## slow_503s_then_revert[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/slow_503s_then_revert[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="slow_503s_then_revert[CONCURRENCY_LIMITER].png" /></td></tr></table>


## slow_503s_then_revert[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/slow_503s_then_revert[ROUND_ROBIN].png" /></td><td><image width=400 src="slow_503s_then_revert[ROUND_ROBIN].png" /></td></tr></table>


## slowdown_and_error_thresholds[CONCURRENCY_LIMITER].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/slowdown_and_error_thresholds[CONCURRENCY_LIMITER].png" /></td><td><image width=400 src="slowdown_and_error_thresholds[CONCURRENCY_LIMITER].png" /></td></tr></table>


## slowdown_and_error_thresholds[ROUND_ROBIN].png
<table><tr><th>develop</th><th>current</th></tr>
<tr><td><image width=400 src="https://media.githubusercontent.com/media/palantir/dialogue/develop/simulation/src/test/resources/slowdown_and_error_thresholds[ROUND_ROBIN].png" /></td><td><image width=400 src="slowdown_and_error_thresholds[ROUND_ROBIN].png" /></td></tr></table>

