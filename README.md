
# star-wars-resistance-social-network

API Docs:  
https://www.postman.com/lively-star-655599/workspace/star-wars-resistance-social-network/overview

Para trocar items de inv    entário, são necessárias duas chamadas POST para o endpoint /api/offer-item, uma de cada rebelde envolvido na troca. Eles devem oferecer itens com o total de pontos equivalentes.

Valores dos itens:
ARMA: 4;
MUNICAO: 3;
AGUA: 2;
COMIDA: 1.

Por exemplo:  
O rebelde com ID 1 oferece uma munição (3 pontos) para o rebelde com ID 7. O body da chamada será:


    {
		"proposerId": 1,
		"receiverId": 7,
		"itemSetList": [
			{
				"item": "MUNICAO",
				"amount": 1
			}
		]
	}  


O rebelde com ID 7 oferece uma água (2 pontos) e uma comida (1 ponto) para o rebelde com ID 1. O body da chamada será:


    {
		"proposerId": 7,
		"receiverId": 1,
		"itemSetList": [
			{
				"item": "AGUA",
				"amount": 1
			},
			{
				"item": "COMIDA",
				"amount": 1
			}
		]
	}
