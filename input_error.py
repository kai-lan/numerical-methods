class InputError(Exception):
    """Exception raised for errors in the input.

    Attributes:
        expression -- input expression in which the error occurred
        message -- explanation of the error

    """
    def __init__(self, expression, message):
        self.expression = expression
        self.message = message


def DimensionError(A, b):
    return InputError(A, ' does not have the same dimension as ['+', '.join(map(str, b))+']')