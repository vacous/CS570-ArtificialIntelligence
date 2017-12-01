import numpy as np 

DISCOUNT_FACTOR= 0.8
# set the energy reward and the transition probability matrix
TRANSITION_MATRIX = {'Drive': {'reward': np.matrix([2.0,0.0,0.0]).T,
'probability': np.matrix([[0.9, 0.1, 0], [0.3, 0.6, 0.1], [0.6, 0, 0.4]])}, 
'Not Drive':{'reward': np.matrix([3.0,1.0,1.0]).T,
'probability': np.matrix([[0.7, 0.3, 0], [0, 0, 1], [0, 0, 1]])}}

### Value Iterate 
state_values = np.matrix([0.0 for _ in range(3)]).T
test_action = None 
def UpdateValue(state_values, transition_mat, discount_factor):
	'''
	update the current node value based on the transition and reward 
	v_(k+1) = max{reward_i + discount_factor * sum_j(p_i_j * v_j(k))}
	'''
	update_values = np.zeros_like(state_values)
	possible_actions = list(transition_mat.keys())
	best_actions = [None for _ in range(len(state_values))]
	for state_idx in range(len(state_values)):
		possible_values = np.zeros((len(possible_actions),1))
		for action_idx in range(len(possible_actions)):
			each_action = possible_actions[action_idx]
			cur_act_result = (transition_mat[each_action]['reward'][state_idx,0]
				+ discount_factor * np.sum(transition_mat[each_action]['probability'][state_idx,:] * state_values))
			possible_values[action_idx,0] = cur_act_result
		best_value_idx = np.argmax(possible_values)
		update_values[state_idx,0] = possible_values[best_value_idx]
		best_actions[state_idx] = possible_actions[best_value_idx]
	return update_values, best_actions

def ValueIterationSolver(ini_state_values, transition_mat, discount_factor, tolerance, max_iter_num, ):
	'''
	iterate the updater until difference smaller than the tolerance
	return the final state values
	'''
	previous_states = ini_state_values 
	previous_actions = None
	for iter_num in range(max_iter_num):
		updated_states, updated_actions = UpdateValue(previous_states, transition_mat, discount_factor)
		if (np.abs(updated_states - previous_states) < tolerance).all():
			return updated_states, updated_actions
		previous_states, previous_actions = updated_states, updated_actions
	print('Max iteration number reached')
	return updated_states, updated_actions

final_values_v, final_policy_v = ValueIterationSolver(state_values, TRANSITION_MATRIX, DISCOUNT_FACTOR, 0.0001, 100)
print('iterate value')
print('final values', final_values_v.T)
print('final policy', final_policy_v)
print('-----------------------------------')


### Policy Iterate 
def StateValueSolver(in_policy, transition_mat, discount_factor):
	'''
	solve for the state value given the policy as a linear system 
	[r] = [I - discount_factor * trans_m][v], solve for v:values
	'''
	possible_actions = list(transition_mat.keys())
	trans_m = np.zeros_like(transition_mat[possible_actions[0]]['probability'])
	rewards = np.zeros_like(transition_mat[possible_actions[0]]['reward'])
	for state_idx in range(len(in_policy)):
		cur_action = in_policy[state_idx]
		rewards[state_idx, 0] = transition_mat[cur_action]['reward'][state_idx,0]
		trans_m[state_idx,:] = transition_mat[cur_action]['probability'][state_idx,:]
	A = np.eye(rewards.shape[0]) - discount_factor * trans_m
	return np.linalg.pinv(A) * rewards

def UpdatePolicy(cur_state_values, transition_mat, discount_factor):
	'''
	compare the result of each action in each state and gives the best actions as the policy 
	'''
	possible_actions = list(transition_mat.keys())
	best_values = np.zeros_like(cur_state_values)
	best_policy = [None for _ in range(len(cur_state_values))]
	for state_idx in range(len(cur_state_values)):
		action_results = []
		for action_idx in range(len(possible_actions)):
			cur_action = possible_actions[action_idx]
			cur_result = (transition_mat[cur_action]['reward'][state_idx,0] 
			+ discount_factor * transition_mat[cur_action]['probability'][state_idx,:] * cur_state_values)
			action_results.append(cur_result)
		max_val_idx = np.argmax(action_results)
		best_values[state_idx,0] = action_results[max_val_idx]
		best_policy[state_idx] = possible_actions[max_val_idx]
	return best_values, best_policy

def PolicyIterationSolver(ini_policy, transition_mat, discount_factor, max_iter_num):
	previous_policy = ini_policy
	for iter_num in range(max_iter_num):
		cur_values =  StateValueSolver(previous_policy, transition_mat, discount_factor)
		cur_values, cur_policy = UpdatePolicy(cur_values, transition_mat, discount_factor)
		if cur_policy == previous_policy:
			return cur_values, cur_policy
		previous_policy = cur_policy
	print('Max iteration num reeached')
	return cur_values.T, cur_policy

ini_policy = ['Drive' for _ in range(3)]
final_values_p, final_policy_p = PolicyIterationSolver(ini_policy, TRANSITION_MATRIX, DISCOUNT_FACTOR, 100)
print('iterate policy')
print('final values', final_values_p.T)
print('final policy', final_policy_p)
print('-----------------------------------')

### Question about changing parameters 
### change discount factor 
print('===================================')
discount_factor_df = DISCOUNT_FACTOR
for each_val in range(1,8):
	discount_factor_df = each_val * 0.1
	print('Current Discount factor', each_val * 0.1)
	final_values_v, final_policy_v = ValueIterationSolver(state_values, TRANSITION_MATRIX, discount_factor_df, 0.0001, 100)
	print('iterate value')
	print('final values', final_values_v.T)
	print('final policy', final_policy_v)
	print('-----------------------------------')
	ini_policy = ['Drive' for _ in range(3)]
	final_values_p, final_policy_p = PolicyIterationSolver(ini_policy, TRANSITION_MATRIX, discount_factor_df, 100)
	print('iterate policy')
	print('final values', final_values_p.T)
	print('final policy', final_policy_p)
	print('-----------------------------------')

### change probability  
print('===================================')
TRANSITION_MATRIX['Not Drive']['probability'][0,:] = [0.1,0.8,0.1]
final_values_v, final_policy_v = ValueIterationSolver(state_values, TRANSITION_MATRIX, discount_factor_df, 0.0001, 100)
print('iterate value')
print('final values', final_values_v.T)
print('final policy', final_policy_v)
print('-----------------------------------')
ini_policy = ['Drive' for _ in range(3)]
final_values_p, final_policy_p = PolicyIterationSolver(ini_policy, TRANSITION_MATRIX, DISCOUNT_FACTOR, 100)
print('iterate policy')
print('final values', final_values_p.T)
print('final policy', final_policy_p)
print('-----------------------------------')
TRANSITION_MATRIX['Not Drive']['probability'][0,:] = [0.3,0.7,0.0]

### change reward 
print('===================================')
TRANSITION_MATRIX['Drive']['reward'][0,0] = 100.0
final_values_v, final_policy_v = ValueIterationSolver(state_values, TRANSITION_MATRIX, discount_factor_df, 0.0001, 100)
print('iterate value')
print('final values', final_values_v.T)
print('final policy', final_policy_v)
print('-----------------------------------')
ini_policy = ['Drive' for _ in range(3)]
final_values_p, final_policy_p = PolicyIterationSolver(ini_policy, TRANSITION_MATRIX, DISCOUNT_FACTOR, 100)
print('iterate policy')
print('final values', final_values_p.T)
print('final policy', final_policy_p)
print('-----------------------------------')
TRANSITION_MATRIX['Drive']['reward'][0,0] = 2.0
