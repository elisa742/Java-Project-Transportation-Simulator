This task is about the design and implementation of a traffic simulation in which road networks are simulated. This allows different scenarios or traffic conditions to be tested and researched in a controlled environment. The simulation consists of a model that abstracts from reality and an execution logic that updates the model step by step. Road network consists in the traffic simulation streets and crossroad node. Cars on the streets are dynamically interact with each other. Several hundred cars must be able to be simulated here. Details of requirments is in pdf "Requirements_Final_Project_Transportation_Simulator".

1.Types of Lane:

1.1 Single Lane: Cars cannot overtake or be overtaken on single lane roads. The order of cars on such a road cannot therefore change, apart from cars entering or leaving the road. 

1.2 Fast Lane: Like single lane, have only one lane. However, a car can overtake the car in front, provided that all distance rules are followed.

2.Overtaking Rules:
![image](https://github.com/user-attachments/assets/1725fc75-d5a8-4e27-8f35-2d4f3fe44aaf)

Example code:
![image](https://github.com/user-attachments/assets/7e15d7ec-5bc4-46f3-835e-7a4eac83be9c)

![image](https://github.com/user-attachments/assets/197eb574-193f-4fd3-8881-ccc85163a755)
