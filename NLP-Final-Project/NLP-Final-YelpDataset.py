import json

# SOURCE: http://www.developintelligence.com/blog/2017/03/predicting-yelp-star-ratings-review-text-python/

# read the data form disk and split into lines
# we use .strip() to remove the final (empty) line


with open("yelp2.json") as f:
	reviews = f.read().strip().split("\n")

# each line of the file is a separate JSON object
reviews = [json.loads(review) for review in reviews]

# we're interested in the text of each review
# and the stars rating, so we load these into
# separate lists

texts = [review['text'] for review in reviews]
stars = [review['stars'] for review in reviews]

print("Made the two arrays")



#Part 2

from collections import Counter

def balance_classes(xs, ys):
	"""Undersample xs, ys to balance classes."""
	freqs = Counter(ys)

	# the least common class is the maximum number we want for all classes
	max_allowable = freqs.most_common()[-1][1]
	num_added = {clss: 0 for clss in freqs.keys()}
	new_ys = []
	new_xs = []
	for i, y in enumerate(ys):
		print(i)
		if num_added[y] < max_allowable:
			new_ys.append(y)
			new_xs.append(xs[i])
			num_added[y] += 1
	return new_xs, new_ys

#Part 3: we can now create a balanced dataset of reviews and stars by running the code (x text, y stars)
print(Counter(stars))
balanced_x, balanced_y = balance_classes(texts, stars)
print(Counter(balanced_y))


#Part 4
from datetime import datetime
from sklearn.feature_extraction.text import TfidfVectorizer

# This vectorizer breaks text into single words and bi-grams
# and then calculates the TF-IDF representation
vectorizer = TfidfVectorizer(ngram_range=(1,2))
t1 = datetime.now()

# the 'fit' builds up the vocabulary from all the reviews
# while the 'transform' step turns each individual text into
# a matrix of numbers
vectors = vectorizer.fit_transform(balanced_x)
print(datetime.now() - t1)


#Part 5: creating a train/test split
from sklearn.model_selection import train_test_split
X_train, X_test, Y_train, Y_test = train_test_split(vectors, balanced_y, test_size = 0.33, random_state = 42)


#Part 6: Fitting a classifier and making predictions (SVM classifier)
from sklearn.svm import LinearSVC

# Initialize the SVM classifier
classifier = LinearSVC()

# train the classifier
t1 = datetime.now()
classifier.fit(X_train, Y_train)
print(datetime.now() - t1)



#Part 7: Prediction
preds = classifier.predict(X_test)
print("Printing predictions:")
print(list([preds[:10]]))
print("tr", Y_test[:10])


#Part8: evaluating our Classifier
# first measure accuracy
from sklearn.metrics import accuracy_score
print(accuracy_score(Y_test, preds))

# measuring using precision and recall with F1 score (using classification report)
from sklearn.metrics import classification_report
print(classification_report(Y_test, preds))

# confusion matrix to find which predictions are most often confused
from sklearn.metrics import confusion_matrix
print(confusion_matrix(Y_test, preds))





#Part 9: CALC POSITIVE AND NEGATIVE REVIEWS
keep = set([1, 2, 4, 5])
# calculate the indices for the examples we want to keep
keep_train_is = [i for i, y in enumerate(Y_train) if y in keep]
keep_test_is = [i for i, y in enumerate(Y_test) if y in keep]

# convert the train set
X_train2 = X_train[keep_train_is, :]
Y_train2 = [Y_train[i] for i in keep_train_is]
Y_train2 = ["n" if (y == 1 or y == 2) else "p" for y in Y_train2]

# convert the test set
X_test2 = X_test[keep_test_is, :]
Y_test2 = [Y_test[i] for i in keep_test_is]
Y_test2 = ["n" if (y == 1 or y == 2) else "p" for y in Y_test2]

classifier.fit(X_train2, Y_train2)
preds = classifier.predict(X_test2)
print(classification_report(Y_test2, preds))
print(confusion_matrix(Y_test2, preds))