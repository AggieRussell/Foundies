@dir = "~/tpreston/projects/foundies-api"

worker_processes 2
#working_directory @dir

timeout 30

listen "tmp/sockets/unicorn.sock", :backlog => 64

listen 8080

pid "tmp/pids/unicorn.pid"

stderr_path "log/unicorn.stderr.log"
stdout_path "log/unicorn.stdout.log"
