require 'rubygems'
require 'sinatra'
require 'bundler'
require 'mongoid'

require File.expand_path '../foundies_api.rb', __FILE__

Bundler.require

Dir[File.dirname(__FILE__) + '/services/*.rb'].each {|file| require file }
Dir[File.dirname(__FILE__) + '/models/*.rb'].each {|file| require file }

Mongoid.load!("config/mongoid.yml", :development)

run FoundiesApi
