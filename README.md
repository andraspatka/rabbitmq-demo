# rabbitmq-demo

Demo project using 3 microservices which are connected with rabbitmq. All services are dockerized and can be deployed to K8s using skaffold/helm

Assignment:
1. Develop 3 web services A, B, C, each in a different technology (python, php, .net core or java).
2. Service A pushes data into RabbitMQ whenever it receives a request, which broadcasts data to B and C in parallel. B and C only display a log when data was fetched from the queue
3. Run 3 instances of Service A
4. Add a loadbalancer based on nginx in front of the 3 instances
5. All services should be containerized via Docker

# Setting up

## Deployment configurations

Requirements:
- Skaffold: https://skaffold.dev/docs/install/
- Helm: https://helm.sh/docs/intro/install/
- Kubernetes cluster (minikube, k3s, Docker-desktop, kind, etc.)
- Docker

mvn io.quarkus:quarkus-maven-plugin:1.13.3.Final:create -DprojectGroupId=andras.patka -DprojectArtifactId=rabbitmq-demo -DclassName="andras.patka.rabbitmq.demo.GreetingResource" -Dpath="/hello"