# Demo

https://github.com/user-attachments/assets/aa1c9fee-2e28-4f2c-bad6-feda64681df7

# Presentation
I started with an HorizontalPager, that gives me in real time the "page offset" for every page. It's a ratio going from ~1 to ~ -1 (let's call it `B`). [1]

https://github.com/user-attachments/assets/98675e27-eedd-4f6d-8c69-cb628e499d8c

Then, depending on that number `B`, I need to know how much I should translate the cards in the "y axis" so they follow a circle. 

![image](https://github.com/user-attachments/assets/5724efea-af70-4055-bb43-12195fe13a88)

Assuming I have an arbitrary point cards should circle around (let's call the y distance between the centre of the cards and this point `A`), we can see that `y = A - C`. Given pythagorean theorem, `B² + C² = A²`. Rearranging, we get `y = A - sqrt(A² - B²)`. Now the cards are moving like a ferris wheel! [2]

https://github.com/user-attachments/assets/8589c34a-31b0-4834-ac79-af1e2a652fa3

Then, I need to know the angle the card should rotate. Let's call that angle `t`. 

![image](https://github.com/user-attachments/assets/da4c5b17-12f7-4054-8e5a-fb5ee68d6e52)

Let's not forget the good old "SOH CAH TOA", which means `t = asin(A / B)`. With the rotation, the illusion is almost perfect [3].

https://github.com/user-attachments/assets/1ddb5539-94a4-4547-ba94-af8674de853f

Just add a bit of trickery at the height of the animation speed to switch the "z index" (ordering who "draws on top of another") between the cards and we get the [final animation](#demo)!

# Thanks
Thank you Adrien F. for the maths help!
