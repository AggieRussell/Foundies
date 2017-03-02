require "rubygems"
require "sinatra/base"
require "json"

class FoundiesApi < Sinatra::Base

  before do
    if request.request_method == 'POST' || request.request_method == 'PUT'
      body_params = request.body.read
      params.merge! JSON.parse(body_params)
    end
  end

  get '/' do
    "Hello everyone"
  end

  get '/users' do
    { users: User.all.map { |user| user.as_json } }.to_json
  end

  get '/user/:username' do |username|
    { user: User.by_username(username).as_json }.to_json
  end

  post '/users' do
    user = User.where(user_params).create
    { user: user.as_json }.to_json
  end

  put '/user/:username' do
    user = User.by_username params[:username]
    user.update user_params
    { user: user.as_json }.to_json
  end

  delete '/user/:username' do |username|
    User.by_username(username).delete
    "You just deleted user id #{username}"
  end

  private

  def user_params
    {
      username: params['user']['username'],
      first_name: params['user']['first_name'],
      last_name: params['user']['last_name']
    }
  end
end
