FROM elixir:1.12.0-rc.1-alpine

WORKDIR src

ADD . .

RUN mix local.hex --force
RUN mix local.rebar --force

RUN mix do deps.get deps.compile
RUN mix do compile

ENTRYPOINT mix run lib/receive.exs --no-halt
