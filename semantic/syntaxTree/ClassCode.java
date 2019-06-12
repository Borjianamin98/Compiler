package semantic.syntaxTree;

/**
 * a class can contain different Node for its code however these classes
 * doesn't have a meaningful superclass.
 * instead, each of them implement this interface so they can treated same
 * (Field, MethodDCL, RecordTypeDCL implements ClassCode)
 */
public interface ClassCode {
}
