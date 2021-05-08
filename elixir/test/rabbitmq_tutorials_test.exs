defmodule RabbitmqTutorialsTest do
  use ExUnit.Case
  doctest RabbitmqTutorials

  test "greets the world" do
    assert RabbitmqTutorials.hello() == :world
  end
end
